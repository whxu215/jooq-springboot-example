package com.github.jooq.example.service;

import static com.github.jooq.example.gen.tables.User.USER;

import com.github.jooq.example.data.Page;
import com.github.jooq.example.gen.tables.pojos.User;
import com.github.jooq.example.gen.tables.records.UserRecord;
import java.time.LocalDateTime;
import java.util.List;
import org.jooq.SelectConditionStep;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService {

  public User createUser(UserRecord user) {
    UserRecord newUser = dslContext.insertInto(USER)
        .set(USER.MOBILE, user.getMobile())
        .set(USER.PASSWORD, user.getPassword())
        .set(USER.ORG_ID, 1)
        .set(USER.STATUS, 1)
        .set(USER.CREATE_TIME, LocalDateTime.now())
        .returning(USER.USER_ID)
        .fetchOne();
    user.setUserId(newUser.getUserId());
    return user.into(User.class);
  }

  public List<User> findByMobile(String mobile) {
    return dslContext.selectFrom(USER).where(
        USER.MOBILE.eq(mobile)).fetch().map(f -> f.into(User.class));
  }

  public Page<List<User>> findByMobileWithPage(String mobile, int currentPage, int pageSize) {
    SelectConditionStep<UserRecord> conditionStep = dslContext.selectFrom(USER)
        .where(USER.MOBILE.like(mobile + "%"));
    return selectPage(conditionStep, User.class, currentPage, pageSize);
  }
}
