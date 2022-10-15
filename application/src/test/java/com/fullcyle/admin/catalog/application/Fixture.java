package com.fullcyle.admin.catalog.application;

import com.fullcyle.admin.catalog.domain.castmember.CastMember;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberType;
import com.fullcyle.admin.catalog.domain.category.Category;
import com.fullcyle.admin.catalog.domain.genre.Genre;
import com.fullcyle.admin.catalog.domain.video.Rating;
import com.fullcyle.admin.catalog.domain.video.Resource;
import com.fullcyle.admin.catalog.domain.video.Video;
import com.github.javafaker.Faker;
import io.vavr.API;

import java.time.Year;
import java.util.Set;

import static com.fullcyle.admin.catalog.domain.castmember.CastMemberType.ACTOR;
import static com.fullcyle.admin.catalog.domain.castmember.CastMemberType.DIRECTOR;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static Integer year() {
        return FAKER.random().nextInt(2020, 2030);
    }

    public static Double duration() {
        return FAKER.options()
                .option(120.0, 15.5, 35.5, 10.0, 2.0, 67.94);
    }

    public static boolean bool() {
        return FAKER.bool().bool();
    }

    public static String title() {
        return FAKER.options()
                .option(
                        " Por outro lado, a implementação do código facilita a criação da garantia da disponibilidade.",
                        "O desenvolvimento contínuo de distintas formas de codificação apresenta tendências no sentido de aprovar a nova topologia dos procedimentos normalmente adotados.",
                        "Ainda assim, existem dúvidas a respeito de como a criticidade dos dados em questão assume importantes níveis de uptime dos paralelismos em potencial.",
                        "Assim mesmo, a preocupação com a TI verde é um ativo de TI dos equipamentos pré-especificados."
                );
    }

    public static final class Categories {
        private static final Category AULAS =
                Category.newCategory("Aulas", "Some description", true);

        public static Category aulas() {
            return AULAS.clone();
        }

    }

    public static final class CastMembers {

        private static final CastMember WESLEY =
                CastMember.newMember("Wesley", ACTOR);

        private static final CastMember GABRIEL =
                CastMember.newMember("Gabriel", ACTOR);

        public static CastMemberType type() {
            return FAKER.options().option(
                    ACTOR,
                    DIRECTOR
            );
        }

        public static CastMember wesley() {
            return CastMember.with(WESLEY);
        }

        public static CastMember gabriel() {
            return CastMember.with(GABRIEL);
        }
    }

    public static final class Genres {
        private static final Genre TECH =
                Genre.newGenre("Tech", true);

        public static Genre tech() {
            return Genre.with(TECH);
        }


    }

    public static final class Videos {

        public static Video video() {
            return Video.newVideo(
                    Fixture.title(),
                    description(),
                    Year.of(Fixture.year()),
                    Fixture.duration(),
                    Fixture.bool(),
                    Fixture.bool(),
                    rating(),
                    Set.of(Categories.aulas().getId()),
                    Set.of(Genres.tech().getId()),
                    Set.of(CastMembers.gabriel().getId(), CastMembers.wesley().getId())
            );
        }

        public static String description() {
            return FAKER.options()
                    .option("""
                                    Desta maneira, a estrutura atual da organização aponta para a melhoria dos relacionamentos
                                    verticais entre as hierarquias. Ainda assim, existem dúvidas a respeito de como o consenso
                                    sobre a necessidade de qualificação é uma das consequências das regras de conduta normativas.
                                    """,
                            """
                                    Não obstante, a execução dos pontos do programa pode nos levar a considerar a 
                                    reestruturação das condições inegavelmente apropriadas.
                                    """
                    );
        }

        public static Rating rating() {
            return FAKER.options()
                    .option(Rating.values());
        }

        public static Resource resource(final Resource.Type type) {
            final String contentType = API.Match(type).of(
                    API.Case(API.$(API.List(Resource.Type.VIDEO, Resource.Type.TRAILER)::contains), "video/mp4"),
                    API.Case(API.$(), "image/jpg")
            );

            final byte[] content = "Conteudo".getBytes();

            return Resource.with(content, contentType, type.name().toLowerCase(), type);
        }


    }
}
