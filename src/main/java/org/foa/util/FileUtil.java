package org.foa.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author 王川源
 * 本类用于帮助文件的各种处理
 */
public class FileUtil {

    public static String readFile(String path) {
        File file = new File(path);
        String content = "";
        try (BufferedReader freader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = freader.readLine()) != null) content += line;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static ResultMessage writeFile(String path, String content) {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (BufferedWriter fwriter = new BufferedWriter(new FileWriter(file))) {
            if (!file.exists())
                file.createNewFile();
            fwriter.write(content);
            fwriter.flush();
            return ResultMessage.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ResultMessage.FAILURE;
        }
    }

    public static ResultMessage deleteFile(String path) {
        File file = new File(path);
        if (file.delete())
            return ResultMessage.SUCCESS;
        else return ResultMessage.FAILURE;
    }

    public static boolean fileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static boolean dirExists(String path) {
        File file = new File(path);
        return file.isDirectory();
    }

    public static void createDirectory(String path) {
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();//建立多级文件夹
    }

    /**
     * 递归删除文件夹
     *
     * @param path
     * @return
     */
    public static boolean deleteDir(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            String[] subFiles = file.list();
            for (String subFile : subFiles)
                deleteDir(path + "/" + subFile);
        }
        return file.delete();
    }

    public static File[] getFileList(String path) {
        File[] files = null;
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            files = dir.listFiles();
            return files;
        } else return files;
    }

    private static boolean isHidden(String path) {
        File file = new File(path);
        return file.isHidden();
    }

    public static String[] getFileNames(String path) {
        String[] fileNames = null;
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            fileNames = dir.list();
            //解决Mac系统下隐藏文件的问题
            List<String> temp = new ArrayList<>(Arrays.asList(fileNames));
            Iterator<String> iterator = temp.iterator();
            while (iterator.hasNext()) {
                String item = iterator.next();
                if (isHidden(path + "/" + item))
                    iterator.remove();
            }
            fileNames = new String[temp.size()];
            temp.toArray(fileNames);
            //解决Linux服务器上读取文件顺序错乱问题
            Arrays.sort(fileNames, (o1, o2) -> {
                if (o1.length() > o2.length())
                    return 1;
                else if (o1.length() < o2.length())
                    return -1;
                else {
                    for (int i = 0; i < o1.length(); i++) {
                        if (o1.charAt(i) == o2.charAt(i))
                            continue;
                        return o1.charAt(i) - o2.charAt(i);
                    }
                }
                return 0;
            });
            return fileNames;
        } else return fileNames;
    }

    public static ResultMessage uploadImage(MultipartFile image, String dirPath) {
        if (image.isEmpty())
            return ResultMessage.FAILURE;
        String imageName = image.getOriginalFilename();
        String uploadPath = dirPath + "/" + imageName;
        File dest = new File(uploadPath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            OutputStream outputStream = new FileOutputStream(dest);
            InputStream imageStream = image.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = imageStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, len);
            }
            imageStream.close();
            outputStream.close();
            return ResultMessage.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultMessage.FAILURE;
    }

    /**
     * 解压文件, 默认zip内只只包含文件，不包含文件夹
     *
     * @param zip
     * @param path
     * @return
     */
    public static ResultMessage unzipFile(MultipartFile zip, String path) {
        createDirectory(path);
        try (ZipInputStream zipInputStream = new ZipInputStream(zip.getInputStream())) {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String zipEntryName = zipEntry.getName().replaceAll("\\+", "/");
                zipEntryName = zipEntryName.substring(zipEntryName.lastIndexOf("/") + 1);
                String outPath = (path + "/" + zipEntryName);
                OutputStream outputStream = new FileOutputStream(outPath);
                byte[] bytes = new byte[1024];
                int len;
                while ((len = zipInputStream.read(bytes)) > 0) {
                    outputStream.write(bytes, 0, len);
                }
                outputStream.close();
                zipInputStream.closeEntry();
            }
            return ResultMessage.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultMessage.FAILURE;
    }

    public static List<String> getZipFileItem(MultipartFile zip) {
        List<String> items = new ArrayList<>();
        try (ZipInputStream zipInputStream = new ZipInputStream(zip.getInputStream())) {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String zipEntryName = zipEntry.getName().replaceAll("\\+", "/");
                zipEntryName = zipEntryName.substring(zipEntryName.lastIndexOf("/") + 1);
                items.add(zipEntryName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    /**
     * @param src 压缩文件的源
     * @param out 压缩文件的输出流
     */
    public static void toZip(String src, OutputStream out) {
        try (ZipOutputStream zos = new ZipOutputStream(out)) {
            File sourceFile = new File(src);
            compress(sourceFile, zos, sourceFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归压缩
     *
     * @param src
     * @param zos
     * @param name 若为xxx/yyy则代表是文件夹
     */
    private static void compress(File src, ZipOutputStream zos, String name) throws IOException {
        byte[] buffer = new byte[1024];
        if (src.isFile()) {
            //添加压缩项
            zos.putNextEntry(new ZipEntry(name));
            int len;
            FileInputStream inputStream = new FileInputStream(src);
            while ((len = inputStream.read(buffer)) != -1) {
                zos.write(buffer, 0, len);
            }
            zos.closeEntry();
            inputStream.close();
        } else {
            //处理文件夹
            File[] files = src.listFiles();
            if (files == null || files.length == 0) {
                //处理空文件夹，依旧保留
                zos.putNextEntry(new ZipEntry(name + "/"));
                zos.closeEntry();
            } else {
                for (File file : files) {
                    compress(file, zos, name + "/" + file.getName());
                }
            }
        }
        //无需关闭zos，上一级调用负责关闭
    }

}
