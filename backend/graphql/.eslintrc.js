module.exports = {
    root: true,
    parser: "@typescript-eslint/parser",
    plugins: ["@typescript-eslint"],
    extends: [
        "airbnb-typescript/base",
        "prettier",
        "seek"
    ],
    parserOptions: {
        ecmaVersion: 2019,
        sourceType: "module",
        project: ["./tsconfig.json"]
    },
    ignorePatterns: ['.eslintrc.js'],
    rules: {
        "@typescript-eslint/camelcase": "off",
        "import/extensions": "off",
        "@typescript-eslint/no-throw-literal": "off",
        "max-len": ["error", { "code": 150 }],
        "no-param-reassign": ["error", { "props": false }],
        "no-underscore-dangle": "off",
        // https://github.com/typescript-eslint/typescript-eslint/blob/master/docs/getting-started/linting/FAQ.md#i-get-errors-from-the-no-undef-rule-about-global-variables-not-being-defined-even-though-there-are-no-typescript-errors
        "no-undef": "off",
        "no-bitwise": "off",
        "@typescript-eslint/no-use-before-define": "off",
        "no-use-before-define": "off",
        // https://github.com/typescript-eslint/typescript-eslint/releases/tag/v4.0.0
        "no-shadow": "off",
        "no-console": "off",
        "@typescript-eslint/no-extra-non-null-assertion": "off",
        // Turned off becausae CDK is using new to create resources
        "no-new": "off",
        // Temporary turn this off because of the aws devDependencies
        "import/no-extraneous-dependencies": "off",
        "func-names": "off",
        "@typescript-eslint/no-shadow": "off"
    }
};
