package com.fullcyle.admin.catalog.application;

import com.fullcyle.admin.catalog.domain.castmember.CastMemberType;
import com.github.javafaker.Faker;

import static com.fullcyle.admin.catalog.domain.castmember.CastMemberType.ACTOR;
import static com.fullcyle.admin.catalog.domain.castmember.CastMemberType.DIRECTOR;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static final class CastMember {

        public static CastMemberType type() {
            return FAKER.options().option(
                    ACTOR,
                    DIRECTOR
            );
        }

    }
}
