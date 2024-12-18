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
        return src(appNpmPath, { read: false })
            .pipe(exec(command, { cwd: appCwd }));
    },
    buildApp: () => {
        const command = 'npm install && npm run build';
        return src(appNpmPath, { read: false })
            .pipe(exec(command, { cwd: appCwd }));
    },
    cleanApp: async () => {
        await fs.remove(appCwd + '/dist');
    }
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

const assets = {
    cleanAssets: async (cb) => {
        await fs.remove('./public/assets');
        cb();
    },
    copyAssets: async (done) => {
        const copyFeatures = src(path.join(apiCwd, 'src/test/resources/features/**'))
            .pipe(dest('./docs/assets'));

        const copyTests = src(path.join(apiCwd, 'src/test/java/com/example/sms/ArchitectureRuleTest**'))
            .pipe(dest('./docs/assets'));

        const mergedStreams = merge(copyFeatures, copyTests);

        mergedStreams.on('end', done);
        mergedStreams.on('error', done);
    },
    copyAssetsPublic: async (done) => {
        const copyFeatures = src(path.join(apiCwd, 'src/test/resources/features/**'))
            .pipe(dest('./public/assets'));

        const copyTests = src(path.join(apiCwd, 'src/test/java/com/example/sms/ArchitectureRuleTest**'))
            .pipe(dest('./public/assets'));

        const mergedStreams = merge(copyFeatures, copyTests);

        mergedStreams.on('end', done);
        mergedStreams.on('error', done);
    },
    moveAssets: async (done) => {
        src(`./docs/assets/**/*`, {encoding: false}).pipe(dest(`./public/assets`))
            .on('end', done);
    },
    publishAssets: (cb) => {
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
    buildAllure: () => {
        console.log("See osp/docker/allure/allure-reports");
        runExecCommand('docker-compose run --rm allure');
    },
    runAllure: () => {
        const command = isWindows ? 'gradlew.bat allureReport' : './gradlew allureReport';
        return src(apiGradlewPath, { read: false })
            .pipe(exec(command, { cwd: apiCwd }));
    },
    copyAllure: async () => {
        const sourcePath = path.join(apiCwd, 'build/reports/allure-report');
        await prepareDirectories(sourcePath);
        return src(sourcePath + '/**')
            .pipe(dest('./ops/docker/allure/allure-reports'));
    },
    cleanAllure: async (cb) => {
        await fs.remove('./ops/docker/allure');
        cb();
    },
    copyAllAllure: async () => {
        // 入力元と出力先ディレクトリ
        const sourcePaths = [
            './app/backend/api/build/allure-results/**',
            './app/frontend/allure-results/**'
        ];
        const outputPath = './ops/docker/allure/allure-results';

        // 出力先ディレクトリがなければ作成
        await fs.ensureDir(outputPath);

        // すべての入力元を確認し、存在するものだけ処理を実施
        for (const sourcePath of sourcePaths) {
            const dirPath = sourcePath.replace('/**', ''); // 入力ディレクトリを抽出
            const pathExists = await fs.pathExists(dirPath); // ディレクトリの存在確認

            if (pathExists) {
                await new Promise((resolve, reject) => {
                    src(sourcePath)
                        .pipe(dest(outputPath))
                        .on('end', resolve) // 処理完了時のコールバック
                        .on('error', reject); // エラー時のコールバック
                });
            } else {
                console.log(`Skipping: Directory does not exist - ${dirPath}`);
            }
        }
    },
    publishAllure: async () => {
        await prepareDirectories('./ops/docker/allure/allure-reports');
        return src('./ops/docker/allure/allure-reports/**')
            .pipe(dest('./public/allure'));
    }
}

const journalNpmPath = path.join(projectRoot, 'docs/journal', 'package.json');
const journalPath = './docs/journal';
const astro = {
    buildAstro: () => {
        const command = 'npm install && npm run build';
        return src(journalNpmPath, { read: false })
            .pipe(exec(command, { cwd: journalPath }));
    },
    cleanAstro: async () => {
        await fs.remove(journalPath + '/dist');
    },
    copyAstro: () => {
        return src(path.join(journalPath, 'dist/**'), {encoding: false})
            .pipe(dest('./public/journal'));
    }
}

exports.assets = assets;
const assetsBuildTasks = () => {
    return series(assets.copyAssets, assets.copyAssetsPublic, assets.moveAssets, assets.publishAssets);
}
exports.assetsBuildTasks = assetsBuildTasks;

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

exports.allure = allure;
const allureBuildTasks = () => {
    return series(allure.cleanAllure, allure.copyAllAllure, allure.buildAllure);
}
exports.allureBuildTasks = allureBuildTasks;
const allureGradleBuildTasks = () => {
    return series(allure.runAllure, allure.copyAllure, allure.copyAllAllure, allure.publishAllure);
}
exports.allureGradleBuildTasks = allureGradleBuildTasks;

const astroBuildTasks = () => {
    return series(astro.buildAstro, astro.copyAstro);
}
exports.astroBuildTasks = astroBuildTasks;
exports.astro = astro.copyAstro;

exports.app = app;
exports.api = api;
const appCleanTasks = () => {
    return parallel(app.cleanApp, api.cleanApi, assets.cleanAssets, jig.cleanJig, jig_erd.cleanJigErd, erd.cleanErd, allure.cleanAllure, astro.cleanAstro);
}
exports.appCleanTasks = appCleanTasks;
const appBuildTasks = () => {
    return parallel(app.buildApp, api.buildApi);
}
exports.appBuildTasks = appBuildTasks;
