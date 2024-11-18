package com.hu.bme.aut.chess.backend.users.profilePicture

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.awt.Image

@Repository
interface ProfilePictureRepository : JpaRepository<ProfilePictureEntity, Long>