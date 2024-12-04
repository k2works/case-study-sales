const { series, parallel } = require('gulp');
const core = require('./ops/gulp/tasks/core');
const custom = require('./ops/gulp/tasks/custom');

const build = series(
    core.webpackBuildTasks(),
    custom.appBuildTasks(),
    parallel(
        custom.assetsBuildTasks(),
        core.asciidoctorBuildTasks(),
        core.marpBuildTasks(),
        core.adrBuildTasks(),
        custom.jigBuildTasks(),
        custom.jigErdBuildTasks(),
        custom.erdBuildTasks(),
        custom.allureGradleBuildTasks(),
    ),
);
exports.build = build;

const start = series(
    series(
        parallel(core.webpack.server, core.asciidoctor.server),
        parallel(core.webpack.watch, core.asciidoctor.watch, core.marp.watch, core.adr.watch),
    ),
    parallel(custom.app.dev, custom.api.dev),
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
