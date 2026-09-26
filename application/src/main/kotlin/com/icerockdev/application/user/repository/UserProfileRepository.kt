package com.icerockdev.application.user.repository

import com.icerockdev.application.user.model.UserProfile
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserProfileRepository : JpaRepository<UserProfile, UUID>
