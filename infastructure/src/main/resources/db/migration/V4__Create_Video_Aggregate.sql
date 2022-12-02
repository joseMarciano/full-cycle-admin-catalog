
CREATE TABLE AUDIO_VIDEO_MEDIA (
    ID CHAR(32) NOT NULL PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL,
    CHECKSUM VARCHAR(255) NOT NULL,
    FILE_PATH VARCHAR(500) NOT NULL,
    ENCODED_PATH VARCHAR(500) NOT NULL,
    MEDIA_STATUS VARCHAR(50) NOT NULL
);


CREATE TABLE IMAGES_MEDIA (
    ID CHAR(32) NOT NULL PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL,
    CHECKSUM VARCHAR(255) NOT NULL,
    FILE_PATH VARCHAR(500) NOT NULL
);

CREATE TABLE VIDEOS (
    ID CHAR(32) NOT NULL PRIMARY KEY,
    TITLE VARCHAR(255) NOT NULL,
    DESCRIPTION VARCHAR(1000) NOT NULL,
    YEAR_LAUNCHED SMALLINT NOT NULL,
    OPENED BOOLEAN NOT NULL DEFAULT FALSE,
    PUBLISHED BOOLEAN NOT NULL DEFAULT FALSE,
    RATING VARCHAR(5),
    DURATION DECIMAL(5,2) NOT NULL,
    CREATED_AT DATETIME(6) NOT NULL,
    UPDATED_AT DATETIME(6) NOT NULL,
    I_VIDEOS CHAR(32),
    I_TRAILERS CHAR(32),
    I_BANNERS CHAR(32),
    I_THUMBNAILS CHAR(32),
    I_THUMBNAILS_HALF CHAR(32),
    CONSTRAINT FK_V_VIDEO_ID FOREIGN KEY(I_VIDEOS) REFERENCES AUDIO_VIDEO_MEDIA(ID) ON DELETE CASCADE,
    CONSTRAINT FK_V_TRAILER_ID FOREIGN KEY(I_TRAILERS) REFERENCES AUDIO_VIDEO_MEDIA(ID) ON DELETE CASCADE,
    CONSTRAINT FK_V_BANNER_ID FOREIGN KEY(I_BANNERS) REFERENCES IMAGES_MEDIA(ID) ON DELETE CASCADE,
    CONSTRAINT FK_V_THUMBNAIL_ID FOREIGN KEY(I_THUMBNAILS) REFERENCES IMAGES_MEDIA(ID) ON DELETE CASCADE,
    CONSTRAINT FK_V_THUMBNAIL_HALF_ID FOREIGN KEY(I_THUMBNAILS_HALF) REFERENCES IMAGES_MEDIA(ID) ON DELETE CASCADE
);


CREATE TABLE VIDEOS_CATEGORIES (
    I_VIDEOS CHAR(32) NOT NULL,
    I_CATEGORIES CHAR(32) NOT NULL,
    CONSTRAINT IDX_VCS_VIDEO_CATEGORY UNIQUE(I_VIDEOS, I_CATEGORIES),
    CONSTRAINT FK_VCS_VIDEO_ID FOREIGN KEY(I_VIDEOS) REFERENCES VIDEOS(ID) ON DELETE CASCADE,
    CONSTRAINT FK_VCS_CATEGORIES_ID FOREIGN KEY(I_CATEGORIES) REFERENCES CATEGORIES(ID) ON DELETE CASCADE
);


CREATE TABLE VIDEOS_GENRES (
    I_VIDEOS CHAR(32) NOT NULL,
    I_GENRES CHAR(32) NOT NULL,
    CONSTRAINT IDX_VGS_VIDEO_CATEGORY UNIQUE(I_VIDEOS, I_GENRES),
    CONSTRAINT FK_VGS_VIDEO_ID FOREIGN KEY(I_VIDEOS) REFERENCES VIDEOS(ID) ON DELETE CASCADE,
    CONSTRAINT FK_VGS_CATEGORIES_ID FOREIGN KEY(I_GENRES) REFERENCES GENRES(ID) ON DELETE CASCADE
);

CREATE TABLE VIDEOS_CAST_MEMBERS (
    I_VIDEOS CHAR(32) NOT NULL,
    I_CAST_MEMBERS CHAR(32) NOT NULL,
    CONSTRAINT IDX_VCMS_VIDEO_CATEGORY UNIQUE(I_VIDEOS, I_CAST_MEMBERS),
    CONSTRAINT FK_VCMS_VIDEO_ID FOREIGN KEY(I_VIDEOS) REFERENCES VIDEOS(ID) ON DELETE CASCADE,
    CONSTRAINT FK_VCMS_CATEGORIES_ID FOREIGN KEY(I_CAST_MEMBERS) REFERENCES CAST_MEMBERS(ID) ON DELETE CASCADE
);
