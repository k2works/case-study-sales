const { series, parallel } = require('gulp');
const core = require('./gulp/tasks/core');
const custom = require('./gulp/tasks/custom');

exports.default = series(
    core.webpackBuildTasks(),
    parallel(
        core.asciidoctorBuildTasks(),
        core.marpBuildTasks(),
    ),
    series(
        parallel(core.webpack.server, core.asciidoctor.server),
        parallel(core.webpack.watch, core.asciidoctor.watch, core.marp.watch),
    ),
);

exports.build = series(
    core.webpackBuildTasks(),
    parallel(
        core.asciidoctorBuildTasks(),
        core.marpBuildTasks(),
    )
);

exports.docs = series(
    parallel(core.asciidoctorBuildTasks(), core.marpBuildTasks()),
    parallel(core.asciidoctor.server, core.asciidoctor.watch, core.marp.watch),
);

exports.slides = series(core.marp.build);

exports.jig = custom.jigBuildTasks();

exports.jig_erd = custom.jigErdBuildTasks();

exports.erd = custom.erdBuildTasks();

exports.buildDocs = series(
    parallel(core.asciidoctorBuildTasks(), core.marpBuildTasks()),
    custom.jigBuildTasks(),
    custom.jigErdBuildTasks(),
    custom.erdBuildTasks(),
);
