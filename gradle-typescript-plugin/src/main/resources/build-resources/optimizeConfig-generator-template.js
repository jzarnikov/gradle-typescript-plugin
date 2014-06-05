var fs = require('fs');
eval(fs.readFileSync("<% print(requirejsConfigPath) %>").toString());

var deps = [];
var moduleName = "<% print(moduleName) %>";
var evaluatedRequireConfig = <% print(requireJsGlobalVarName) %>;
if (<% print(includeLibsInCombinedJs) %>) {
    for (var item in evaluatedRequireConfig.paths) {
          deps.push(item);
    }
}
var optimizeConfig = {
        paths: evaluatedRequireConfig.paths,
        shim: evaluatedRequireConfig.shim,
        baseUrl: evaluatedRequireConfig.baseUrl,
        out: moduleName + "-all.js",
        name: moduleName,
        include: deps
}

fs.writeFileSync(moduleName + "-optimizeConfig.js", JSON.stringify(optimizeConfig));
