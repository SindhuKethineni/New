package com.fashionapp.filestorage;


import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
    
	public void init();

	public void store(MultipartFile file);
	public void storeUserProfileImage(MultipartFile file);
	public Stream<Path> loadFiles(); 
    public Resource loadFile(String filename) ;
    public Resource loadFile(String[] filename);
    public Resource loadprofileImage(String filename) ;
    public void deleteAll();
	public void deletefile();
	public void storemultiple(List<MultipartFile> file);
        
}
