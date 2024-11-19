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

    @Transactional
    fun saveProfilePicture(bytes: ByteArray): ProfilePictureEntity {
        var profilePic = ProfilePictureEntity()
        val owner = userService.findAuthenticatedUser()!!


        owner.getProfilePicture()?.let { oldProfilePic ->
            oldProfilePic.setContent(bytes)
            return profilePictureRepository.save(oldProfilePic)
        }


        //set new
        profilePic.setOwner(owner)
        profilePic.setContent(bytes)


        profilePic = profilePictureRepository.save(profilePic)
        owner.setProfilePicture(profilePic)
        userService.saveUser(owner)

        return profilePic
    }


}