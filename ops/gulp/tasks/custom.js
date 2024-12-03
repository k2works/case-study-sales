const { src, dest, watch, series, parallel} = require('gulp');
const exec = require('gulp-exec');
const gulpRename = require('gulp-rename');
const fs = require('fs-extra');
const path = require('path');
const merge = require('gulp-merge');

// プロジェクトルートのディレクトリを取得
const projectRoot = process.cwd();
const apiGradlewPath = path.join(projectRoot, 'app/backend/api', 'gradlew');
const apiCwd = path.join(projectRoot, 'app/backend/api');
const appCwd = path.join(projectRoot, 'app/frontend');

// Windows環境かどうかをチェック
const isWindows = process.platform === 'win32';

const api = {
    dev: () => {
        const command = isWindows ? 'gradlew.bat bootRun' : './gradlew bootRun';
        return src(apiGradlewPath, { read: false })
            .pipe(exec(command, { cwd: apiCwd }));
    }
}

const app = {
    dev: () => {
        const command = 'npm install && npm run dev';
        return src(apiGradlewPath, { read: false })
            .pipe(exec(command, { cwd: appCwd }));
    }
}

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

const DOCKER_OUTPUT_PATH = "./ops/docker/schemaspy/output";
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

const assets = {
    clean: async (cb) => {
        await fs.remove('./docs/assets');
        await fs.remove('./public/assets');
        cb();
    },
    copyDocs: async (done) => {
        const copyFeatures = src(path.join(apiCwd, 'src/test/resources/features/**'))
            .pipe(dest('./docs/assets'));

        const copyTests = src(path.join(apiCwd, 'src/test/java/com/example/sms/ArchitectureRuleTest**'))
            .pipe(dest('./docs/assets'));

        const mergedStreams = merge(copyFeatures, copyTests);

        mergedStreams.on('end', done);
        mergedStreams.on('error', done);
    },
    copyPublic: async (done) => {
        const copyFeatures = src(path.join(apiCwd, 'src/test/resources/features/**'))
            .pipe(dest('./public/assets'));

        const copyTests = src(path.join(apiCwd, 'src/test/java/com/example/sms/ArchitectureRuleTest**'))
            .pipe(dest('./public/assets'));

        const mergedStreams = merge(copyFeatures, copyTests);

        mergedStreams.on('end', done);
        mergedStreams.on('error', done);
    },
    move: async (done) => {
        src(`./docs/assets/**/*`, {encoding: false}).pipe(dest(`./public/assets`))
            .on('end', done);
    },
    publish: (cb) => {
        const content = `
        <!DOCTYPE html>
        <html lang="ja">
        <html>
            <head>
                <title>リリース</title>
            </head>
            <body>
                <h1>リリース</h1>
                <h2>v0.1.0</h2>

                <div>
                    <p><a href="./v0_1_0/jig/index.html">JIG</a></p>
                    <img src="./v0_1_0/jig-erd/library-er-summary.svg" alt="Summary">
                </div>
                
                <h2>v0.2.0</h2>
                <div>
                    <p><a href="./v0_2_0/jig/index.html">JIG</a></p>
                    <img src="./v0_2_0/jig-erd/library-er-summary.svg" alt="Summary">
                </div>

                <p>Powered by <a href="https://github.com/dddjava/jig">Jig</a> </p>
           </body>
        </html>
    `;
        fs.writeFileSync('work.html', content);
        src('work.html')
            .pipe(gulpRename('index.html'))
            .pipe(dest('public/assets/release'))
            .on('end', () => {
                fs.unlinkSync('work.html'); // Delete work.html after rename & move
            });
        cb();
    },
};
const prepareDirectories = async (path) => {
    await fs.mkdirs(path);
}
const allure = {
    build: () => {
         console.log("See osp/docker/allure/allure-reports");
         runExecCommand('docker-compose run --rm allure');
    },
    build_gradle: () => {
        const command = isWindows ? 'gradlew.bat allureReport' : './gradlew allureReport';
        return src(apiGradlewPath, { read: false })
            .pipe(exec(command, { cwd: apiCwd }));
    },
    copy_build_gradle: async () => {
        const sourcePath = path.join(apiCwd, 'build/reports/allure-report');
        await prepareDirectories(sourcePath);
        return src(sourcePath + '/**')
            .pipe(dest('./ops/docker/allure/allure-reports'));
    },
    clean: async (cb) => {
        await fs.remove('./ops/docker/allure');
        cb();
    },
    copy: () => {
        return src('./app/backend/api/build/allure-results/**')
            .pipe(dest('./ops/docker/allure/allure-results'))
            .pipe(src('./app/frontend/allure-results/**'))
            .pipe(dest('./ops/docker/allure/allure-results'));
    },
    publish: async () => {
        await prepareDirectories('./ops/docker/allure/allure-reports');
        return src('./ops/docker/allure/allure-reports/**')
            .pipe(dest('./public/allure'));
    }
}

exports.assets = assets;
const assetsBuildTasks = () => {
    return series(assets.copyDocs, assets.copyPublic, assets.move, assets.publish);
}
exports.assetsBuildTasks = assetsBuildTasks;

const erdBuildTasks = () => {
    return series(
        parallel(
            //erd.buildMySQL,
            erd.buildPostgresql
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

exports.app = app;
exports.api = api;
exports.jig = jig;
exports.jig_erd = jig_erd;
exports.erd = erd;
exports.erdBuildTasks = erdBuildTasks
exports.jigBuildTasks = jigBuildTasks
exports.jigErdBuildTasks = jigErdBuildTasks

exports.allure = allure;
const allureBuildTasks = () => {
    return series(allure.clean, allure.copy, allure.build);
}
exports.allureBuildTasks = allureBuildTasks;
const allureGradleBuildTasks = () => {
    return series(allure.build_gradle, allure.copy_build_gradle, allure.publish);
}
exports.allureGradleBuildTasks = allureGradleBuildTasks;