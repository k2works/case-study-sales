const { src, dest, watch, series, parallel} = require('gulp');
const exec = require('gulp-exec');
const gulpRename = require('gulp-rename');
const fs = require('fs-extra');
const path = require('path');

// プロジェクトルートのディレクトリを取得
const projectRoot = process.cwd();
const apiGradlewPath = path.join(projectRoot, 'api', 'gradlew');
const apiCwd = path.join(projectRoot, 'api');

// Windows環境かどうかをチェック
const isWindows = process.platform === 'win32';

const jig = {
    build: () => {
        const command = isWindows ? 'gradlew.bat build -x test' : './gradlew build -x test';
        return src(apiGradlewPath, { read: false })
            .pipe(exec(command, { cwd: apiCwd }));
    },
    docs: () => {
        const command = isWindows ? 'gradlew.bat jigReports' : './gradlew jigReports';
        return src(apiGradlewPath, { read: false })
            .pipe(exec(command, { cwd: apiCwd }));
    },
    cleanDoc: async () => {
        await fs.remove("./public/jig");
    },
    cleanApi: () => {
        const command = isWindows ? 'gradlew.bat clean' : './gradlew clean';
        return src(apiGradlewPath, {read: false})
            .pipe(exec(command, {cwd: apiCwd}));
    },
    copy: () => {
        return src(path.join(apiCwd, 'build/jig/**'))
            .pipe(dest('./public/jig'));
    },
    watch: (cb) => {
        watch(path.join(apiCwd, 'src/**/*.java'), series(jig.docs, jig.copy));
        cb();
    },
};

const jig_erd = {
    build: () => {
        const command = isWindows ? 'gradlew.bat test --tests "com.example.sms.Erd.run"' : './gradlew test --tests "com.example.sms.Erd.run"';
        return src(apiGradlewPath, { read: false })
            .pipe(exec(command, { cwd: apiCwd }));
    },
    clean: async () => {
        await fs.remove("./public/jig-erd");
    },
    copy: () => {
        return src(path.join(apiCwd, 'build/jig-erd/**'))
            .pipe(dest('./public/jig-erd'));
    },
    watch: (cb) => {
        watch(path.join(apiCwd, 'src/**/*.java'), series(jig_erd.build, jig_erd.copy));
        cb();
    },
    publish: (cb) => {
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

const DOCKER_OUTPUT_PATH = "./docker/schemaspy/output";
const PUBLIC_ERD_PATH = "./public/erd";
const BASE_SRC_PATH = "./";

const runExecCommand = (command) => {
    return src(BASE_SRC_PATH).pipe(exec(command));
};

const erd = {
    buildMySQL: () => {
        return runExecCommand('npm run db:schemaspy:mysql');
    },
    buildPostgresql: () => {
        return runExecCommand('npm run db:schemaspy:postresql');
    },
    clean: async (cb) => {
        await fs.remove(DOCKER_OUTPUT_PATH);
        cb();
    },
    copy: () => {
        return src(`${DOCKER_OUTPUT_PATH}/**`, {encoding: false})
            .pipe(dest(PUBLIC_ERD_PATH));
    },
};

const erdBuildTasks = () => {
    return series(
        parallel(
            erd.buildMySQL, erd.buildPostgresql
        ),
        erd.copy
    );
}

const jigBuildTasks = () => {
    return series(jig.cleanDoc, jig.cleanApi, jig.build, jig.docs, jig.copy);
}

const jigErdBuildTasks = () => {
    return series(jig_erd.clean, jig_erd.build, jig_erd.copy, jig_erd.publish);
}

exports.jig = jig;
exports.jig_erd = jig_erd;
exports.erd = erd;
exports.erdBuildTasks = erdBuildTasks
exports.jigBuildTasks = jigBuildTasks
exports.jigErdBuildTasks = jigErdBuildTasks
