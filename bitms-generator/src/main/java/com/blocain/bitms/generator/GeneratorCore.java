package com.blocain.bitms.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.blocain.bitms.generator.model.IModel;
import com.blocain.bitms.generator.utils.FileHelper;
import com.blocain.bitms.generator.utils.PropertiesProvider;
import com.blocain.bitms.generator.utils.StringTemplate;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 核心文件生成器
 * <p>File：GeneratorCore.java</p>
 * <p>Title: GeneratorCore</p>
 * <p>Description:GeneratorCore</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class GeneratorCore
{
    // 模型所在的地址
    public File           templateRootDir = null;
    
    // 提供template数据模型
    public IModelProvider modelProvider   = null;
    
    public GeneratorCore()
    {
    }
    
    /**
     * 生成模型
     * 
     * @throws Exception
     */
    public void generateByModelProvider() throws Exception
    {
        generateByModel(modelProvider.getModel());
    }
    
    /**
     * 根据模型生成
     * 
     * @param model
     * @throws Exception
     */
    @SuppressWarnings({"rawtypes", "deprecation"})
    private void generateByModel(IModel model) throws Exception
    {
        System.out.println("***************************************************************");
        System.out.println("* 开始生成模型:" + model.getDisplayDescription());
        Configuration config = new Configuration();
        config.setDirectoryForTemplateLoading(templateRootDir);
        config.setNumberFormat("###############");
        config.setBooleanFormat("true,false");
        List templateFiles = new ArrayList();
        FileHelper.listFiles(templateRootDir, templateFiles);
        for (int i = 0; i < templateFiles.size(); i++)
        {
            File templateFile = (File) templateFiles.get(i);
            String templateRelativePath = FileHelper.getRelativePath(templateRootDir, templateFile);
            String outputFilePath = templateRelativePath;
            try
            {
                if (templateFile.isDirectory() || templateFile.isHidden())
                {
                    continue;
                }
                if (templateRelativePath.trim().equals(""))
                {
                    continue;
                }
                if (templateFile.getName().toLowerCase().endsWith(".include"))
                {
                    continue;
                }
                generateFile(model, config, templateRelativePath, outputFilePath);
            }
            catch (Exception e)
            {
                throw new RuntimeException("生成文件 '" + model.getDisplayDescription() + "' 出错,模板:" + templateRelativePath, e);
            }
        }
        System.out.println("* 生成模型:" + model.getDisplayDescription() + "结束");
        System.out.println("***************************************************************");
    }
    
    /**
     * 生成文件
     * 
     * @param model
     * @param config
     * @param templateRelativePath
     * @param outputFilePath
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    private void generateFile(IModel model, Configuration config, String templateRelativePath, String outputFilePath) throws Exception
    {
        Template template = config.getTemplate(templateRelativePath);
        String targetFilename = getTargetFilename(model, outputFilePath);
        Map templateDataModel = getTemplateDataModel(model);
        File absoluteOutputFilePath = getAbsoluteOutputFilePath(targetFilename);
        if (absoluteOutputFilePath.exists())
        {
            System.out.println("[跳过]\t 模型:" + templateRelativePath + " 到 " + targetFilename);
        }
        else
        {
            System.out.println("[生成]\t 模型:" + templateRelativePath + " 到 " + targetFilename);
            saveNewOutputFileContent(template, templateDataModel, absoluteOutputFilePath);
        }
    }
    
    /**
     * 根据模型生成对应的文件
     * 
     * @param model
     * @param templateFilepath
     * @return {@link String}
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    private String getTargetFilename(IModel model, String templateFilepath) throws Exception
    {
        Map fileModel = getFilepathDataModel(model);
        String targetFilename = new StringTemplate(templateFilepath, fileModel).toString();
        return targetFilename;
    }
    
    /**
     * 得到生成"文件目录/文件路径"的Model
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map getFilepathDataModel(IModel model) throws Exception
    {
        Map fileModel = new HashMap();
        fileModel.putAll(PropertiesProvider.getProperties());
        model.mergeFilePathModel(fileModel);
        return fileModel;
    }
    
    /**
     * 得到FreeMarker的Model
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map getTemplateDataModel(IModel model)
    {
        Map map = new HashMap();
        map.putAll(PropertiesProvider.getProperties());
        map.put(model.getTemplateModelName(), model);
        return map;
    }
    
    /**
     * 取绝对输出路径
     * 
     * @param targetFilename
     * @return {@link String}
     */
    private File getAbsoluteOutputFilePath(String targetFilename)
    {
        String stuff = targetFilename.substring(targetFilename.indexOf(".") + 1, targetFilename.length());
        String outRoot = "";
        if ("java".equals(stuff) || "xml".equals(stuff))
        {
            outRoot = getJavaOutRootDir();
        }
        else
        {
            outRoot = getPageOutRootDir();
        }
        File outputFile = new File(outRoot, targetFilename);
        outputFile.getParentFile().mkdirs();
        return outputFile;
    }
    
    /**
     * 保存新输出文件内容
     * 
     * @param template
     * @param model
     * @param outputFile
     * @throws IOException
     * @throws TemplateException
     */
    @SuppressWarnings("rawtypes")
    private void saveNewOutputFileContent(Template template, Map model, File outputFile) throws IOException, TemplateException
    {
        FileWriter out = new FileWriter(outputFile);
        template.process(model, out);
        out.close();
    }
    
    /**
     * 删除文件夹及其所有文件
     * 
     * @throws IOException
     */
    public void removeJavaFile() throws IOException
    {
        String outRoot = getJavaOutRootDir();
        FileUtils.deleteDirectory(new File(outRoot));
        System.out.println("[删除文件夹]	" + outRoot);
    }
    
    /**
     * 删除文件夹及其所有文件
     * 
     * @throws IOException
     */
    public void removePageFile() throws IOException
    {
        String outRoot = getPageOutRootDir();
        FileUtils.deleteDirectory(new File(outRoot));
        System.out.println("[删除文件夹]	" + outRoot);
    }
    
    /**
     * 取输出文件路径
     * 
     * @return {@link String}
     */
    private String getJavaOutRootDir()
    {
        return PropertiesProvider.getProperties().getProperty("java_file_output_directory");
    }
    
    /**
     * 取输出文件路径
     * 
     * @return {@link String}
     */
    private String getPageOutRootDir()
    {
        return PropertiesProvider.getProperties().getProperty("page_file_output_directory");
    }
}
