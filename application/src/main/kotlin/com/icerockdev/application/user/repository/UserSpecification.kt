package com.icerockdev.application.user.repository

import com.icerockdev.util.and
import com.icerockdev.util.emptySpecification
import com.icerockdev.application.user.dto.UserFilterDto
import com.icerockdev.application.user.model.User
import com.icerockdev.domain.EntitySpecification
import com.icerockdev.util.equal
import org.springframework.data.jpa.domain.Specification

object UserSpecification: EntitySpecification<User, UserFilterDto> {
    override fun create(filter: UserFilterDto?): Specification<User> {
        return filter?.let {
            and(byPhone(it))
                .and(byEmail(it))
                .and(byStatus(it))
                .and(byRole(it))
                .and(byIsTest(it))
                .and(byId(it))
        }
            ?: emptySpecification()
    }

    private fun byPhone(filterDto: UserFilterDto): Specification<User>? {
        return filterDto.phone?.let { phone ->
            User::phone.equal(phone)
        }
    }

    private fun byEmail(filterDto: UserFilterDto): Specification<User>? {
        return filterDto.email?.let { email ->
            User::email.equal(email)
        }
    }

    private fun byStatus(filterDto: UserFilterDto): Specification<User>? {
        return filterDto.status?.let { status ->
            User::status.equal(status)
        }
    }

    private fun byRole(filterDto: UserFilterDto): Specification<User>? {
        return filterDto.role?.let { role ->
            User::role.equal(role)
        }
    }

    private fun byIsTest(filterDto: UserFilterDto): Specification<User>? {
        return filterDto.isTest?.let { isTest ->
            User::isTest.equal(isTest)
        }
    }

    private fun byId(filterDto: UserFilterDto): Specification<User>? {
        return filterDto.id?.let { id ->
            User::id.equal(id)
        }
    }

    // region Example
    /*
    Example:
    Task: у сущности User есть связь один ко многим с сущностью Card, задача найти всех User, у которых есть карточки
    с именем из списка cardNames
    class User {
        val id: Int,

        @BatchSize(size = 10)
        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
        val cards: List<Cards> = emptyList()
    }

    class Card {
        val id: Int,
        val name: String,

        @NotNull
        @Column(name = "user_id", insertable = false, updatable = false)
        val userId: Int,
    }
    Solution:
    private fun byAmenitiesCodes(filterDto: UserFilterDto): Specification<User>? {
        return filterDto.cardNames?.let { cardNames ->
            Specification { root, query, _ ->
                // будем возвращать id всех User, у которых Card names принадлежат cardNames
                val subquery: Subquery<Int> = query.subquery(Int::class.java)

                //Создаем root класс для Card, чтобы получать данные колонок
                val subRoot: Root<Card> = subquery.from(Card::class.java)

                // Достаем все user id с условием = Card name in cardNames
                subquery.select(subRoot.get(Card::userId))
                subquery.where(
                    subRoot
                        .get(Card::name)
                        .`in`(cardNames)
                )

                // Получаем всех User, у которых id in subquery id list
                (root.get(User::id)).`in`(subquery)
            }
        }
    }
    */
    // endregion
}
