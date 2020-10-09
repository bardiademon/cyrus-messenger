package com.bardiademon.CyrusMessenger.Model.Database.Images;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadedImagesRepository extends JpaRepository <UploadedImages, Long>
{
}
