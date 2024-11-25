module.exports = {
    moduleFileExtensions: [
        "js",
        "ts",
        "tsx"
    ],
    testMatch: [
        "**/src/**/*.test.ts",
        "**/src/**/*.test.tsx"
    ],
    roots: [
        "<rootDir>/src"
    ],
    preset: "ts-jest",
    testEnvironment: "jest-environment-jsdom",
    transform: {
        '^.+\\.jsx?$': 'babel-jest',
        '^.+\\.tsx?$': 'babel-jest',
        '\\.css$': 'jest-transform-css',
    },
    moduleNameMapper: {
        '\\.(css|scss)$': 'identity-obj-proxy',
        "\\.(gif|ttf|eot|svg|png)$": "<rootDir>/test/__mocks__/fileMock.js",
    },
};
