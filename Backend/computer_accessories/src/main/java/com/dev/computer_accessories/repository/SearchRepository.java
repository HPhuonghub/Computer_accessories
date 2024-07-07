package com.dev.computer_accessories.repository;

import com.dev.computer_accessories.dto.response.PageResponse;
import com.dev.computer_accessories.model.User;
import com.dev.computer_accessories.repository.criteria.SearchCriteria;
import com.dev.computer_accessories.repository.criteria.UserSearchCriteriaQueryConsumer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Repository
public class SearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public PageResponse<?> getAllUsersWithSortByColumnAndSearch(int pageNo, int pageSize, String search, String sortBy) {
        log.info("Execute search user with keyword {}", search);

        StringBuilder sqlQuery = new StringBuilder("select new com.dev.computer_accessories.dto.response.UserDetailResponse(u.id, u.fullName, u.email, u.phone) from User u where 1=1");
        if(StringUtils.hasLength(search)) {
            sqlQuery.append(" and lower(:fullName) like lower(:fullName)");
            sqlQuery.append(" or lower(:email) like lower(:email)");
        }

        if (StringUtils.hasLength(sortBy)) {
            //fullName:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()) {
                sqlQuery.append(String.format(" order by u.%s %s", matcher.group(1), matcher.group(3)));
            }
        }

        Query selectQuery = entityManager.createQuery(sqlQuery.toString());

        selectQuery.setFirstResult(pageNo);
        selectQuery.setMaxResults(pageSize);
        if(StringUtils.hasLength(search)) {
            selectQuery.setParameter("fullName", String.format("%%%s%%", search));
            selectQuery.setParameter("email", String.format("%%%s%%", search));
        }
        List users = selectQuery.getResultList();

        System.out.println(users);
        //query ra list user

        //query so record
        StringBuilder sqlCountQuery = new StringBuilder("select count(*) from User u where 1=1");
        if(StringUtils.hasLength(search)) {
            sqlCountQuery.append(" and lower(:fullName) like lower(?1)");
            sqlCountQuery.append(" or lower(:email) like lower(?2)");
        }

        Query selectCountQuery = entityManager.createQuery(sqlCountQuery.toString());
        if(StringUtils.hasLength(search)) {
            selectCountQuery.setParameter(1, String.format("%%%s%%", search));
            selectCountQuery.setParameter(2, String.format("%%%s%%", search));
        }

        Long totalElements = (Long) selectCountQuery.getSingleResult();
        System.out.println(totalElements);

        Page<?> page = new PageImpl<Object>(users, PageRequest.of(pageNo, pageSize), totalElements);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(page.getTotalPages())
                .items(page.stream().toList())
                .build();
    }

    public PageResponse<?> advanceSearchCriteria(int pageNo, int pageSize, String sortBy, String... search) {
        // firstNAme: T, lastName: T

        List<SearchCriteria> criteriaList = new ArrayList<>();
        // 1. lay ra danh sach user
        if(search != null ) {
            for( String sort : search) {
                // firstName:value
                Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(.*)");
                Matcher matcher = pattern.matcher(sort);
                if(matcher.find()) {
                    // todo
                    criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }

        // 2. lay ra so luong ban ghi
        List<User> users = getUsers(pageNo, pageSize, criteriaList, sortBy);


        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(0)
                .items(users)
                .build();
    }

    private List<User> getUsers(int pageNo, int pageSize, List<SearchCriteria> criteriaList, String sortBy) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        // Xu li cac dieu kien tim kiem
        Predicate predicate = criteriaBuilder.conjunction();
        UserSearchCriteriaQueryConsumer queryConsumer = new UserSearchCriteriaQueryConsumer(criteriaBuilder, predicate, root);


        criteriaList.forEach(queryConsumer);
        predicate = queryConsumer.getPredicate();

        query.where(predicate);

        return entityManager.createQuery(query).setFirstResult(pageNo).setMaxResults(pageSize).getResultList();
    }
}
