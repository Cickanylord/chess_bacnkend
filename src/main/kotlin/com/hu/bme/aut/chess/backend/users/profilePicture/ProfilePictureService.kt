package com.hu.bme.aut.chess.backend.users.profilePicture

import com.hu.bme.aut.chess.backend.users.UserRepository
import com.hu.bme.aut.chess.backend.users.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional



@Service
@Transactional
class ProfilePictureService @Autowired constructor(
    private val profilePictureRepository: ProfilePictureRepository,
    private val userService: UserService
) {
    fun findProfilePictureById(id: Long): ProfilePictureEntity? {
        return profilePictureRepository.findById(id).orElse(null)
    }

    fun saveProfilePicture(bytes: ByteArray): ProfilePictureEntity {
        val profilePic = ProfilePictureEntity()

        profilePic.setOwner(userService.findAuthenticatedUser()!!)
        profilePic.setContent(bytes)

        return profilePictureRepository.save(profilePic)
    }


}