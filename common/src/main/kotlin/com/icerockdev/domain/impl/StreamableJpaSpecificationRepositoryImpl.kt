package com.icerockdev.domain.impl

import com.icerockdev.domain.StreamableJpaSpecificationRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root
import java.util.stream.Stream
import org.hibernate.jpa.HibernateHints
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.query.QueryUtils


open class StreamableJpaSpecificationRepositoryImpl<ENTITY>(
    private val entityManager: EntityManager
) : StreamableJpaSpecificationRepository<ENTITY> {

    override fun stream(specification: Specification<ENTITY>?, sort: Sort?, clazz: Class<ENTITY>): Stream<ENTITY> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val criteriaQuery: CriteriaQuery<ENTITY> = criteriaBuilder.createQuery(clazz)
        val root: Root<ENTITY> = criteriaQuery.from(clazz)
        criteriaQuery.select(root)
        if (specification != null) {
            criteriaQuery.where(specification.toPredicate(root, criteriaQuery, criteriaBuilder))
        }

        if (sort?.isSorted == true) {
            criteriaQuery.orderBy(QueryUtils.toOrders(sort, root, criteriaBuilder))
        }

        return entityManager.createQuery(criteriaQuery)
            .setHint(
                HibernateHints.HINT_FETCH_SIZE,
                "1"
            )
            .resultStream
    }
}
