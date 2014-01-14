var fs = require('fs');
eval(fs.readFileSync("<% print(requirejsConfigPath) %>").toString());

var deps = [];
var moduleName = "<% print(moduleName) %>";
if (<% print(includeLibsInCombinedJs) %>) {
    for (var item in requireConfig.paths) {
          deps.push(item);
    }
}
var optimizeConfig = {
        paths: requireConfig.paths,
        shim: requireConfig.shim,
        baseUrl: requireConfig.baseUrl,
        out: moduleName + "-all.js",
        name: moduleName,
        include: deps
}

fs.writeFileSync(moduleName + "-optimizeConfig.js", JSON.stringify(optimizeConfig));
