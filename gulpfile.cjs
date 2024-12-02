const { series, parallel } = require('gulp');
const core = require('./ops/gulp/tasks/core');
const custom = require('./ops/gulp/tasks/custom');

const build = series(
    core.webpackBuildTasks(),
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

exports.default = series(
    series(
        parallel(core.webpack.server, core.asciidoctor.server),
        parallel(core.webpack.watch, core.asciidoctor.watch, core.marp.watch, core.adr.watch),
    ),
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

exports.buildDocs = series(
    parallel(core.asciidoctorBuildTasks(), core.marpBuildTasks()),
    custom.jigBuildTasks(),
    custom.jigErdBuildTasks(),
    custom.erdBuildTasks(),
);

exports.dev = series(
    build,
    parallel(core.webpack.server, core.asciidoctor.server),
    parallel(core.webpack.watch, core.asciidoctor.watch, core.marp.watch),
    parallel(custom.app.dev, custom.api.dev),
);

exports.allure = custom.allureBuildTasks();
