package com.blog.app.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public String uploadImage(String path, MultipartFile file) throws IOException {
	
		//file name
		String name=file.getOriginalFilename();
		//sample.png
		
		
		//generate file with random name
		String randomId=UUID.randomUUID().toString();
		String fileName=randomId.concat(name.substring(name.lastIndexOf(".")));
		
		//file path
		String filePath=path+File.separator+fileName;
		
		//create folder if it doesnt exist
		File f=new File(path);
		if(!f.exists()) {
			f.mkdir();
		}
		
		//file copy
		Files.copy(file.getInputStream(), Paths.get(filePath));
		
		return fileName;
	}

	@Override
	public InputStream getResource(String path, String fileName) throws FileNotFoundException {
		String filePath=path+File.separator+fileName;
		InputStream inputStream=new FileInputStream(filePath);
		return inputStream;
	}

}
