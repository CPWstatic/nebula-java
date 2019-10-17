package com.vesoft.nebula.tools;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineParser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class GeoImporterTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {

    }

    @Test
    public void testIndexCells() throws Exception {
        Method method = GeoImporter.class.getDeclaredMethod("indexCells", double.class, double.class);
        method.setAccessible(true);
        Object obj = method.invoke(GeoImporter.getInstance(), 30.28522, 120.01338);
        List<Long> result = (ArrayList<Long>) obj;
        Assert.assertTrue(result.size() > 0);
    }

    @Test
    public void testOptions() throws Exception {
        Options options = new Options();
        CmdLineParser cmdLineParser = new CmdLineParser(options);

        String[] argsHelp = {"-h"};
        cmdLineParser.parseArgument(argsHelp);
        Assert.assertTrue(options.help);

        argsHelp[0] = "--help";
        cmdLineParser.parseArgument(argsHelp);

        String[] args = {
                "-a=127.0.0.1:3699",
                "-f=./tools/importer/src/test/Resources/geo.csv",
                "-b=16",
                "-n=geo",
                "-d=./error",
                "-u=user",
                "-p=password"};
        cmdLineParser.parseArgument(args);
        Assert.assertEquals(options.addresses, "127.0.0.1:3699");
        Assert.assertEquals(options.file.getPath(), "./tools/importer/src/test/Resources/geo.csv");
        Assert.assertEquals(options.batchSize.intValue(), 16);
        Assert.assertEquals(options.spaceName, "geo");
        Assert.assertEquals(options.user, "user");
        Assert.assertEquals(options.password, "password");
    }

    @Test
    public void testReadContent() throws Exception {
        Options options = new Options();
        CmdLineParser cmdLineParser = new CmdLineParser(options);

        String[] args = {
                "-a=127.0.0.1:3699",
                "-f=./src/test/Resources/geo.csv",
                "-b=16",
                "-name=geo",
                "-d=./error",
                "-u=user",
                "-p=password"};
        cmdLineParser.parseArgument(args);

        Method setGeoOptions = GeoImporter.class.getDeclaredMethod("setOptions", Options.class);
        setGeoOptions.setAccessible(true);
        setGeoOptions.invoke(GeoImporter.getInstance(), options);

        Method readContent = GeoImporter.class.getDeclaredMethod("readContent");
        readContent.setAccessible(true);
        readContent.invoke(GeoImporter.getInstance());
    }
}
