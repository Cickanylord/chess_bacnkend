package com.hu.bme.aut.chess.backend.users.profilePicture

import com.hu.bme.aut.chess.backend.users.UserService
import com.hu.bme.aut.chess.backend.users.dataTransferObject.UserResponseDTO
import com.hu.bme.aut.chess.backend.users.toUserResponseDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.awt.Image


@RestController
@RequestMapping("/api/image")
class ProfilePictureController @Autowired constructor(
    private val profilePictureService: ProfilePictureService,

) {

    @GetMapping("/{imageId}")
    fun getProfilePicture(@PathVariable("imageId") imageId: Long): ResponseEntity<ImageResponseDto> {
        val profilePic = profilePictureService
            .findProfilePictureById(imageId)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(profilePic.toImageResponseDto())
    }

    @PostMapping
    @Throws(Exception::class)
    fun uploadImage(@RequestParam multipartImage: MultipartFile): ResponseEntity<Long?> {
        val profilePic = profilePictureService.saveProfilePicture(multipartImage.bytes)

        return ResponseEntity.ok(profilePic.getId())
    }

}

fun ProfilePictureEntity.toImageResponseDto(): ImageResponseDto {
    return ImageResponseDto(
        id = this.getId(),
        profilePicture = this.getContent(),
        owner = this.getOwner().toUserResponseDTO()
    )
}

data class ImageResponseDto(
    val id: Long,
    val profilePicture: ByteArray,
    val owner: UserResponseDTO?
)