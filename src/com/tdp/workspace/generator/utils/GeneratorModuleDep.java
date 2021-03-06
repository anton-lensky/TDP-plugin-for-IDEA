package com.tdp.workspace.generator.utils;

import com.tdp.workspace.generator.Constants;
import com.tdp.workspace.generator.fileutils.ReadFileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * Created by Siarhei Nahel on 21.05.2016.
 */
public class GeneratorModuleDep {
    private final String repoPath;
    private final NavigableSet<String> baseModules;
    private NavigableSet<String> allModules = new TreeSet<>();

    public GeneratorModuleDep(String repoPath, NavigableSet<String> baseModules) {
        this.repoPath = repoPath;
        this.baseModules = baseModules;
    }

    private void generateAllDeps(String name) throws FileNotFoundException {
        String fileName =  repoPath + Constants.SLASH + name + Constants.SLASH
                + Constants.BUILD_DIR  + Constants.FILE_NAME_DEP;
        File file = new File(fileName);
        if (file.exists()) {
            NavigableSet<String> modules = ReadFileUtil.getModuleNames(file);
            for (String moduleName : modules) {
                if (!allModules.contains(moduleName)){
                    allModules.add(moduleName);
                    generateAllDeps(moduleName);
                }
            }
        }

    }

    public NavigableSet<String> getModuleNames() throws FileNotFoundException {
        for (String m : baseModules) {
            allModules.add(m);
            generateAllDeps(m);
        }
        ModulesFilter filter = new ModulesFilter(allModules, repoPath);
        return filter.getModuleFiles();
    }
}