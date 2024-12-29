const { src, dest, watch, series, parallel} = require('gulp');
const exec = require('gulp-exec');
const gulpRename = require('gulp-rename');
const fs = require('fs-extra');
const path = require('path');
const merge = require('gulp-merge');

// プロジェクトルートのディレクトリを取得
const projectRoot = process.cwd();
const appNpmPath = path.join(projectRoot, 'app/frontend', 'package.json');
const apiGradlewPath = path.join(projectRoot, 'app/backend/api', 'gradlew');
const apiCwd = path.join(projectRoot, 'app/backend/api');
const appCwd = path.join(projectRoot, 'app/frontend');

// Windows環境かどうかをチェック
const isWindows = process.platform === 'win32';

const api = {
    devApi: () => {
        const command = isWindows ? 'gradlew.bat bootRun' : './gradlew bootRun';
        return src(apiGradlewPath, { read: false })
            .pipe(exec(command, { cwd: apiCwd }));
    },
    buildApi: () => {
        const command = isWindows ? 'gradlew.bat build' : './gradlew build';
        return src(apiGradlewPath, { read: false })
            .pipe(exec(command, { cwd: apiCwd }));
    },
    cleanApi: () => {
        const command = isWindows ? 'gradlew.bat clean' : './gradlew clean';
        return src(apiGradlewPath, { read: false })
            .pipe(exec(command, { cwd: apiCwd }));
    }
}

const app = {
    devApp: () => {
        const command = 'npm run dev';
        return src(appNpmPath, {read: false})
            .pipe(exec(command, {cwd: appCwd}));
    },
    buildApp: () => {
        const command = 'npm install && npm run build';
        return src(appNpmPath, {read: false})
            .pipe(exec(command, {cwd: appCwd}));
    },
    cleanApp: async () => {
        await fs.remove(appCwd + '/dist');
    },
    openApp: () => {
        const command = isWindows ? 'start' : 'open';
        return src(appNpmPath, {read: false})
            .pipe(exec(`${command} http://localhost:5173`));
    },
}

const jig = {
    buildJig: () => {
        const command = isWindows ? 'gradlew.bat build -x test' : './gradlew build -x test';
        return src(apiGradlewPath, { read: false })
            .pipe(exec(command, { cwd: apiCwd }));
    },
    runJig: () => {
        const command = isWindows ? 'gradlew.bat jigReports' : './gradlew jigReports';
        return src(apiGradlewPath, { read: false })
            .pipe(exec(command, { cwd: apiCwd }));
    },
    cleanJig: async () => {
        await fs.remove("./public/jig");
    },
    cleanApi: () => {
        const command = isWindows ? 'gradlew.bat clean' : './gradlew clean';
        return src(apiGradlewPath, {read: false})
            .pipe(exec(command, {cwd: apiCwd}));
    },
    copyJig: () => {
        return src(path.join(apiCwd, 'build/jig/**'))
            .pipe(dest('./public/jig'));
    },
    watchJig: (cb) => {
        watch(path.join(apiCwd, 'src/**/*.java'), series(jig.docs, jig.copy));
        cb();
    },
};

const jig_erd = {
    runJigErd: () => {
        const command = isWindows ? 'gradlew.bat test --tests "com.example.sms.Erd.run"' : './gradlew test --tests "com.example.sms.Erd.run"';
        return src(apiGradlewPath, { read: false })
            .pipe(exec(command, { cwd: apiCwd }));
    },
    cleanJigErd: async () => {
        await fs.remove("./public/jig-erd");
    },
    copyJigErd: () => {
        return src(path.join(apiCwd, 'build/jig-erd/**'))
            .pipe(dest('./public/jig-erd'));
    },
    watchJigErd: (cb) => {
        watch(path.join(apiCwd, 'src/**/*.java'), series(jig_erd.build, jig_erd.copy));
        cb();
    },
    publishJigErd: (cb) => {
        const content = `<!DOCTYPE html>
    <html>
        <head>
            <title>JEG-ERD</title>
        </head>
        <body>
            <h1>JEG-ERD</h1>
            <p>Entity Relationship Diagram</p>
            <h2>Overview</h2>
            <img src="library-er-overview.svg" alt="Overview">
            <h2>Summary</h2>
            <img src="library-er-summary.svg" alt="Summary">
            <h2>Details</h2>
            <img src="library-er-detail.svg" alt="Details">
            
            <p>Powered by <a href="https://github.com/irof/jig-erd/tree/master">Jig-Erd</a> </p>
       </body>
    </html>`;
        fs.writeFileSync('jigerd.html', content);
        src('jigerd.html')
            .pipe(gulpRename('index.html'))
            .pipe(dest('public/jig-erd'))
            .on('end', () => {
                fs.unlinkSync('jigerd.html'); // Delete jigerd.html after rename & move
            });
        cb();
    },
};

const DOCKER_OUTPUT_PATH = "./ops/docker/schemaspy/output";
const PUBLIC_ERD_PATH = "./public/erd";
const BASE_SRC_PATH = "./";

const runExecCommand = (command) => {
    return src(BASE_SRC_PATH).pipe(exec(command));
};

const erd = {
    buildMySQLErd: () => {
        return runExecCommand('npm run db:schemaspy:mysql');
    },
    buildPostgresqlErd: () => {
        return runExecCommand('npm run db:schemaspy:postresql');
    },
    cleanErd: async (cb) => {
        await fs.remove(DOCKER_OUTPUT_PATH);
        cb();
    },
    copyErd: () => {
        return src(`${DOCKER_OUTPUT_PATH}/**`, {encoding: false})
            .pipe(dest(PUBLIC_ERD_PATH));
    },
};

const erdBuildTasks = () => {
    return series(
        parallel(
            //erd.buildMySQL,
            erd.buildPostgresqlErd
        ),
        erd.copyErd
    );
}

const jigBuildTasks = () => {
    return series(jig.runJig, jig.copyJig);
}
exports.jig = jig;
exports.jigBuildTasks = jigBuildTasks

const jigErdBuildTasks = () => {
    return series(jig_erd.runJigErd, jig_erd.copyJigErd, jig_erd.publishJigErd);
}
exports.jig_erd = jig_erd;
exports.jigErdBuildTasks = jigErdBuildTasks

exports.erd = erd;
exports.erdBuildTasks = erdBuildTasks

exports.app = app;
exports.api = api;
const appCleanTasks = () => {
    return parallel(app.cleanApp, api.cleanApi, jig.cleanJig, jig_erd.cleanJigErd, erd.cleanErd);
}
exports.appCleanTasks = appCleanTasks;
const appBuildTasks = () => {
    return parallel(app.buildApp, api.buildApi);
}
exports.appBuildTasks = appBuildTasks;