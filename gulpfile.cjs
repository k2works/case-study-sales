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
    custom.jigBuildTasks(),
    custom.jigErdBuildTasks(),
    custom.erdBuildTasks(),
);
exports.build = build;

const start = series(
    series(
        parallel(core.webpack.server, core.asciidoctor.server),
        parallel(core.webpack.watch, core.asciidoctor.watch, core.marp.watch, core.adr.watch),
    ),
    parallel(custom.app.devApp, custom.api.devApi, custom.app.openApp),
);
exports.default = start;

exports.dev = series(
    build,
    start,
);

exports.docs = series(
    parallel(core.asciidoctorBuildTasks(), core.marpBuildTasks()),
    core.adrBuildTasks(),
    parallel(core.asciidoctor.server, core.asciidoctor.watch, core.marp.watch, core.adr.watch),
);

exports.slides = series(core.marp.build);

exports.jig = custom.jigBuildTasks();

exports.jig_erd = custom.jigErdBuildTasks();

exports.erd = custom.erdBuildTasks();
