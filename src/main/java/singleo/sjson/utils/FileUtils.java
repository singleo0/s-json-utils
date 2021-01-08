package singleo.sjson.utils;

import java.io.*;

/**
 * 文件处理工具类
 *
 * @author zn
 */
public class FileUtils
{
    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os 输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException
    {
        FileInputStream fis = null;
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0)
            {
                os.write(b, 0, length);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件
     * @return
     */
    public static boolean deleteFile(String filePath)
    {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists())
        {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 获取文件后缀
     *
     * @param file 上传的文件
     * @throws FileSizeLimitExceededException 如果超出最大大小
     */
    public static final String getExtension(File file)
    {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 读取文件转化为字符串
     *
     * @param path 绝对路径
     * @return
     * @throws IOException
     */
    public static String getStringFromFile(String path) throws IOException {
        byte[] strBuffer = null;
        int flen = 0;
        File file = new File(path);
        InputStream in = new FileInputStream(file);
        flen = (int) file.length();
        strBuffer = new byte[flen];
        in.read(strBuffer, 0, flen);
        in.close();
        return new String(strBuffer);
    }

}

