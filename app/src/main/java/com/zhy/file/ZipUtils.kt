package com.zhy.file

import java.io.*
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipException
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

/**
 * Java utils 实现的Zip工具
 * https://www.freebasic.cn/p/1679.html
 */
object ZipUtils {
	private const val BUFF_SIZE = 1024 * 1024 // 1M Byte

	/**
	 * 批量压缩文件（夹）
	 * @param resFileList 要压缩的文件（夹）列表
	 * @param zipFile 生成的压缩文件
	 * @throws IOException 当压缩过程出错时抛出
	 */
	@Throws(IOException::class)
	fun zipFiles(resFileList: Collection<File>, zipFile: File) {
		val zipout = ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile), BUFF_SIZE))
		for (resFile in resFileList) {
			zipFile(resFile, zipout, "")
		}
		zipout.close()
	}

	/**
	 * 批量压缩文件（夹）
	 * @param resFileList 要压缩的文件（夹）列表
	 * @param zipFile 生成的压缩文件
	 * @param comment 压缩文件的注释
	 * @throws IOException 当压缩过程出错时抛出
	 */
	@Throws(IOException::class)
	fun zipFiles(resFileList: Collection<File>, zipFile: File?, comment: String?) {
		val zipout = ZipOutputStream(BufferedOutputStream(FileOutputStream(
			zipFile), BUFF_SIZE))
		for (resFile in resFileList) {
			zipFile(resFile, zipout, "")
		}
		zipout.setComment(comment)
		zipout.close()
	}

	/**
	 * 解压缩一个文件 160314
	 * @param zipFile 压缩文件
	 * @param folderPath 解压缩的目标目录
	 * @throws IOException 当解压缩过程出错时抛出
	 */
	@Throws(ZipException::class, IOException::class)
	fun upZipFile(zipFile: File?, folderPath: String) {
		val desDir = File(folderPath)
		if (!desDir.exists()) {
			desDir.mkdirs()
		}
		val zf = ZipFile(zipFile)
		val entries: Enumeration<*> = zf.entries()
		while (entries.hasMoreElements()) {
			val entry = entries.nextElement() as ZipEntry
			//ss:if it's a filedir,it need to be opened 160314
			//String name = entry.getName();
			//if(name.endsWith(File.separator))
			//continue;


			val `in`: InputStream = zf.getInputStream(entry)
			var str = folderPath + File.separator + entry.name
			//str = String(str.toByteArray(charset("8859_1")), "GB2312")
			str = str.toByteArray(Charsets.ISO_8859_1).toString(Charsets.UTF_8)
			val desFile = File(str)
			if (!desFile.exists()) {
				val fileParentDir: File = desFile.parentFile
				if (!fileParentDir.exists()) {
					fileParentDir.mkdirs()
				}
				desFile.createNewFile()
			}
			val out: OutputStream = FileOutputStream(desFile)
			val buffer = ByteArray(BUFF_SIZE)
			var realLength: Int
			while (`in`.read(buffer).also { realLength = it } > 0) {
				out.write(buffer, 0, realLength)
			}
			`in`.close()
			out.close()
		}
	}

	/**
	 * 解压文件名包含传入文字的文件
	 * @param zipFile 压缩文件
	 * @param folderPath 目标文件夹
	 * @param nameContains 传入的文件匹配名
	 * @throws ZipException 压缩格式有误时抛出
	 * @throws IOException IO错误时抛出
	 */
	@Throws(ZipException::class, IOException::class)
	fun upZipSelectedFile(zipFile: File?, folderPath: String,
						  nameContains: String?): ArrayList<File> {
		val fileList = ArrayList<File>()
		val desDir = File(folderPath)
		if (!desDir.exists()) {
			desDir.mkdir()
		}
		val zf = ZipFile(zipFile)
		val entries: Enumeration<*> = zf.entries()
		while (entries.hasMoreElements()) {
			val entry = entries.nextElement() as ZipEntry
			if (entry.name.contains(nameContains!!)) {
				val `in`: InputStream = zf.getInputStream(entry)
				var str = folderPath + File.separator + entry.name
				//str = String(str.toByteArray(charset("8859_1")), "GB2312")
				str = str.toByteArray(Charsets.ISO_8859_1).toString(Charsets.UTF_8)
// str.getBytes("GB2312"),"8859_1" 输出
				// str.getBytes("8859_1"),"GB2312" 输入
				val desFile = File(str)
				if (!desFile.exists()) {
					val fileParentDir: File = desFile.parentFile
					if (!fileParentDir.exists()) {
						fileParentDir.mkdirs()
					}
					desFile.createNewFile()
				}
				val out: OutputStream = FileOutputStream(desFile)
				val buffer = ByteArray(BUFF_SIZE)
				var realLength: Int
				while (`in`.read(buffer).also { realLength = it } > 0) {
					out.write(buffer, 0, realLength)
				}
				`in`.close()
				out.close()
				fileList.add(desFile)
			}
		}
		return fileList
	}

	/**
	 * 获得压缩文件内文件列表
	 * @param zipFile 压缩文件
	 * @return 压缩文件内文件名称
	 * @throws ZipException 压缩文件格式有误时抛出
	 * @throws IOException 当解压缩过程出错时抛出
	 */
	@Throws(ZipException::class, IOException::class)
	fun getEntriesNames(zipFile: File): ArrayList<String> {
		val entryNames = ArrayList<String>()
		val entries = getEntriesEnumeration(zipFile)
		while (entries.hasMoreElements()) {
			val entry = entries.nextElement() as ZipEntry
			//str = str.toByteArray(Charsets.ISO_8859_1).toString(Charsets.UTF_8)
			entryNames.add(getEntryName(entry).toByteArray(Charsets.UTF_8).toString(Charsets.ISO_8859_1))
		}
		return entryNames
	}

	/**
	 * 获得压缩文件内压缩文件对象以取得其属性
	 * @param zipFile 压缩文件
	 * @return 返回一个压缩文件列表
	 * @throws ZipException 压缩文件格式有误时抛出
	 * @throws IOException IO操作有误时抛出
	 */
	@Throws(ZipException::class, IOException::class)
	fun getEntriesEnumeration(zipFile: File): Enumeration<*> {
		val zf = ZipFile(zipFile)
		return zf.entries()
	}

	/**
	 * 取得压缩文件对象的注释
	 * @param entry 压缩文件对象
	 * @return 压缩文件对象的注释
	 * @throws UnsupportedEncodingException
	 */
	@Throws(UnsupportedEncodingException::class)
	fun getEntryComment(entry: ZipEntry): String {
		//entryNames.add(getEntryName(entry).toByteArray(Charsets.UTF_8).toString(Charsets.ISO_8859_1))
		return entry.comment.toByteArray(Charsets.UTF_8).toString(Charsets.ISO_8859_1)
	}

	/**
	 * 取得压缩文件对象的名称
	 * @param entry 压缩文件对象
	 * @return 压缩文件对象的名称
	 * @throws UnsupportedEncodingException
	 */
	@Throws(UnsupportedEncodingException::class)
	fun getEntryName(entry: ZipEntry): String {
		//entryNames.add(getEntryName(entry).toByteArray(Charsets.UTF_8).toString(Charsets.ISO_8859_1))
		return entry.name.toByteArray(Charsets.UTF_8).toString(Charsets.ISO_8859_1)
	}

	/**
	 * 压缩文件
	 * @param resFile 需要压缩的文件（夹）
	 * @param zipout 压缩的目的文件
	 * @param rootpath 压缩的文件路径
	 * @throws FileNotFoundException 找不到文件时抛出
	 * @throws IOException 当压缩过程出错时抛出
	 */
	@Throws(FileNotFoundException::class, IOException::class)
	private fun zipFile(resFile: File, zipout: ZipOutputStream, rootpath: String) {
		var rootpath = rootpath
		rootpath = (rootpath + (if (rootpath.trim { it <= ' ' }.isEmpty()) "" else File.separator)
				+ resFile.name)
		rootpath = rootpath.toByteArray(Charsets.ISO_8859_1).toString(Charsets.UTF_8)
		if (resFile.isDirectory) {
			val fileList: Array<File> = resFile.listFiles()
			for (file in fileList) {
				zipFile(file, zipout, rootpath)
			}
		} else {
			val buffer = ByteArray(BUFF_SIZE)
			val `in` = BufferedInputStream(FileInputStream(resFile), BUFF_SIZE)
			zipout.putNextEntry(ZipEntry(rootpath))
			var realLength: Int
			while (`in`.read(buffer).also { realLength = it } != -1) {
				zipout.write(buffer, 0, realLength)
			}
			`in`.close()
			zipout.flush()
			zipout.closeEntry()
		}
	}
}