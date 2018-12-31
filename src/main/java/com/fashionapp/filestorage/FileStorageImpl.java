package com.fashionapp.filestorage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageImpl implements FileStorage {

	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	private final Path rootLocation = Paths.get("filestorage");
	private final Path profieImageLocation = Paths.get("profileimages");

	@Override
	public void store(MultipartFile file) {
		try {
			System.out.println("fileName : = " + file);
			Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			throw new RuntimeException("FAIL! -> message = " + e.getMessage());
		}
	}

	@Override
	public void storeUserProfileImage(MultipartFile file) {
		try {
			Files.copy(file.getInputStream(), this.profieImageLocation.resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			throw new RuntimeException("FAIL! -> message = " + e.getMessage());
		}
	}

	@Override
	public void storemultiple(List<MultipartFile> files) {

		for (MultipartFile file : files) {

			try {
				byte[] bytes = file.getBytes();
				Path path = this.rootLocation.resolve(file.getOriginalFilename());
				Files.write(path, bytes);
				log.info("inserted!");
			} catch (Exception e) {
				throw new RuntimeException("FAIL! -> message = " + e.getMessage());
			}
		}
	}

	@Override
	public void deletefile() {
		try {
			FileSystemUtils.deleteRecursively(profieImageLocation.toAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		File directory = new File(String.valueOf("filestorage"));
		File directory2 = new File(String.valueOf("profileimages"));

		try {
			if (!directory.exists())
				Files.createDirectory(rootLocation);
			if (!directory2.exists())
				Files.createDirectory(profieImageLocation);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize storage!");
		}
	}

	@Override
	public Stream<Path> loadFiles() {
		try {
			return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation))
					.map(this.rootLocation::relativize);
		} catch (IOException e) {
			throw new RuntimeException("\"Failed to read stored file");
		}
	}

	@Override
	public Resource loadFile(String[] filename) {
		try {
			for (String fi : filename) {
				Path file = rootLocation.resolve(fi);
				Resource resource = new UrlResource(file.toUri());
				if (resource.exists() || resource.isReadable()) {
					return resource;
				} else {
					throw new RuntimeException("FAIL!");
				}
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error! -> message = " + e.getMessage());
		}
		return null;
	}

	@Override
	public Resource loadFile(String filename) {
		try {
			Path file = rootLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("FAIL!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error! -> message = " + e.getMessage());
		}
	}

	@Override
	public Resource loadprofileImage(String filename) {
		try {
			Path file = profieImageLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("FAIL!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error! -> message = " + e.getMessage());
		}
	}

}
