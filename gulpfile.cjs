const { series, parallel } = require('gulp');
const core = require('./ops/gulp/tasks/core');
const custom = require('./ops/gulp/tasks/custom');

const build = series(
    core.webpackBuildTasks(),
    core.asciidoctorBuildTasks(),
    core.marpBuildTasks(),
    core.adrBuildTasks(),
    custom.appCleanTasks(),
    custom.appBuildTasks(),
    custom.assetsBuildTasks(),
    custom.jigBuildTasks(),
    custom.jigErdBuildTasks(),
    custom.erdBuildTasks(),
    custom.allureGradleBuildTasks(),
    custom.astroBuildTasks(),
    custom.wiki.buildWiki,
);
exports.build = build;

const start = series(
    series(
        parallel(core.webpack.server, core.asciidoctor.server),
        parallel(core.webpack.watch, core.asciidoctor.watch, core.marp.watch, core.adr.watch),
    ),
    parallel(custom.app.devApp, custom.api.devApi, custom.app.openApp, custom.wiki.openWiki, custom.wiki.startWiki,custom.astro.openAstro, custom.astro.runAstro),
);
exports.default = start;

exports.dev = series(
    build,
    start,
);

exports.docs = series(
    parallel(custom.assetsBuildTasks(), core.asciidoctorBuildTasks(), core.marpBuildTasks()),
    core.adrBuildTasks(),
    parallel(core.asciidoctor.server, core.asciidoctor.watch, core.marp.watch, core.adr.watch),
);

exports.slides = series(core.marp.build);

exports.jig = custom.jigBuildTasks();

exports.jig_erd = custom.jigErdBuildTasks();

exports.erd = custom.erdBuildTasks();

exports.allure = custom.allureBuildTasks();

exports.allureBuild = custom.allureGradleBuildTasks();
