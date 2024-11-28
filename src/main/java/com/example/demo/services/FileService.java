package com.example.demo.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exceptions.FilesException;

@Service
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
public class FileService {
	
	private final String uploadDirectory = "src/main/resources/uploads/";
	private final String apiUploads = "/api/file/";
	
	public Resource getFile(String filename) throws IOException {
		Path filePath = Paths.get(uploadDirectory).resolve(filename).normalize();
        
		if(!Files.exists(filePath)) {
			throw new IOException("El archivo no existe");
		}
		
        Resource resource = new PathResource(filePath.toString());
        return resource;
	}

	public String uploadFile(MultipartFile file) throws Exception {
		try {
			String fileName = UUID.randomUUID().toString();
			byte[] bytes = file.getBytes();
			String fileOriginalName = file.getOriginalFilename();

			long fileSize = file.getSize();
			long maxFileSize = 10 * 1024 * 1024;

			if (fileSize > maxFileSize) {
				throw new FilesException("El tamaño del archivo es de mas de 10 MB");
			}

			if (fileOriginalName.endsWith(".jpg") || fileOriginalName.endsWith(".png") || fileOriginalName.endsWith(".jpeg")
					|| fileOriginalName.endsWith(".bmp") || fileOriginalName.endsWith(".gif")
					|| fileOriginalName.endsWith(".tiff") || fileOriginalName.endsWith(".tif")
					|| fileOriginalName.endsWith(".webp") || fileOriginalName.endsWith(".ico")
					|| fileOriginalName.endsWith(".svg") || fileOriginalName.endsWith(".mp4")
					|| fileOriginalName.endsWith(".avi") || fileOriginalName.endsWith(".mov")
					|| fileOriginalName.endsWith(".mkv") || fileOriginalName.endsWith(".flv")
					|| fileOriginalName.endsWith(".wmv") || fileOriginalName.endsWith(".xls")
					|| fileOriginalName.endsWith(".xlsx") || fileOriginalName.endsWith(".csv")
					|| fileOriginalName.endsWith(".ods") || fileOriginalName.endsWith(".tsv")||fileOriginalName.endsWith(".zip")) {
					String fileExtension = fileOriginalName.substring(fileOriginalName.lastIndexOf("."));
					String newFile = fileName+fileExtension;
					File folder =  new File(uploadDirectory);
					
					if(!folder.exists()) {
						folder.mkdirs();
					}
					
					Path path = Paths.get(uploadDirectory).resolve(newFile).normalize();
					Files.write(path, bytes);
					return Paths.get(apiUploads).resolve(newFile).normalize().toString().replace("\\", "/");
			} else {
				throw new FilesException("Ese Tipo de archivo no esta permitido, y este es el nombre:" + fileOriginalName + " y pesa: " +fileSize);
			}
		} catch (Exception e) {
			throw new FilesException(e.getMessage());
		}
	}

}
