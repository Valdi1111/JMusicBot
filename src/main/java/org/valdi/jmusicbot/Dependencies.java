package org.valdi.jmusicbot;

import org.valdi.SuperApiX.common.dependencies.Dependency;

public class Dependencies {

    public static final Dependency JAR_RELOCATOR = Dependency
            .builder("JAR_RELOCATOR")
            .setGroupId("me.lucko")
            .setArtifactId("jar-relocator")
            .setVersion("1.3")
            .setChecksum("mmz3ltQbS8xXGA2scM0ZH6raISlt4nukjCiU2l9Jxfs=")
            .setAutoLoad(false)
            .build();

    public static final Dependency GOOGLE_GSON = Dependency
            .builder("GOOGLE_GSON")
            .setGroupId("com{}google{}code{}gson")
            .setArtifactId("gson")
            .setVersion("2.8.6")
            .setChecksum("yPtIOQVNKAswM/gA0fWpfeLwKOuLoutFitKH5Tbz8l8=")
            .setAutoLoad(true)
            .build();

    public static final Dependency GOOGLE_GUAVA = Dependency
            .builder("GOOGLE_GUAVA")
            .setGroupId("com{}google{}guava")
            .setArtifactId("guava")
            .setVersion("28.1-jre")
            .setChecksum("ML64uFJ70HxudH538akhIsLynVfONHRhpKVesm44LaQ=")
            .setAutoLoad(true)
            .build();

    public static final Dependency GOOGLE_GUAVA_FAILUREACCESS = Dependency
            .builder("GOOGLE_GUAVA_FAILUREACCESS")
            .setGroupId("com{}google{}guava")
            .setArtifactId("failureaccess")
            .setVersion("1.0.1")
            .setChecksum("oXHuTHNN0tqDfksWvp30Zhr6typBra8x64Tf2vk2yiY=")
            .setAutoLoad(true)
            .build();

    public static final Dependency COMMONS_LANG3 = Dependency
            .builder("COMMONS_LANG3")
            .setGroupId("org{}apache{}commons")
            .setArtifactId("commons-lang3")
            .setVersion("3.9")
            .setChecksum("3i4dzc8++ReozoWGYaBnJqmpRPKOM61/ngi+pE3DwjA=")
            .setAutoLoad(true)
            .build();

    public static final Dependency LOG4J_SLF4J_BINDING = Dependency
            .builder("LOG4J_SLF4J_BINDING")
            .setGroupId("org{}apache{}logging{}log4j")
            .setArtifactId("log4j-slf4j-impl")
            .setVersion("2.12.1")
            .setChecksum("PZYgr8PNWFJ6GCtw58ERtyiQRpicDQSlDkaw7DHcE4o=")
            .setAutoLoad(true)
            .build();

    public static final Dependency CAFFEINE = Dependency
            .builder("CAFFEINE")
            .setGroupId("com{}github{}ben-manes{}caffeine")
            .setArtifactId("caffeine")
            .setVersion("2.8.0")
            .setChecksum("sRB6QJe+RRWpI6Vbxj2gTkEeaWSqBFvs4bx6y4SHLtc=")
            .setAutoLoad(true)
            .build();

    public static final Dependency HTML_COMPRESSOR = Dependency
            .builder("HTML_COMPRESSOR")
            .setGroupId("com{}googlecode{}htmlcompressor")
            .setArtifactId("htmlcompressor")
            .setVersion("1.4")
            .setChecksum("vsE8IURq8k8vJpN7NhczqKMqH2hRu67v/OrGrIU3zJw=")
            .setAutoLoad(true)
            .build();

    public static final Dependency HTTP_REQUEST = Dependency
            .builder("HTML_COMPRESSOR")
            .setGroupId("com{}github{}kevinsawicki")
            .setArtifactId("http-request")
            .setVersion("6.0")
            .setChecksum("Zz3QKt5BlqtRHoEqaFfayutXFt6oOqIX26ZUgktdVHY=")
            .setAutoLoad(true)
            .build();

    public static final Dependency CONFIGURATE_CORE = Dependency
            .builder("CONFIGURATE_CORE")
            .setGroupId("me{}lucko{}configurate")
            .setArtifactId("configurate-core")
            .setVersion("3.5")
            .setChecksum("J+1WnX1g5gr4ne8qA7DuBadLDOsZnOZjwHbdRmVgF6c=")
            .setAutoLoad(true)
            .build();

    public static final Dependency YAML = Dependency
            .builder("YAML")
            .setGroupId("org{}yaml")
            .setArtifactId("snakeyaml")
            .setVersion("1.25")
            .setChecksum("tQ7zMYfn3JIrJtvk3Q/bOpzzSedaCLlSaZAVSO7lRus=")
            .setAutoLoad(true)
            .build();

    public static final Dependency CONFIGURATE_YAML = Dependency
            .builder("CONFIGURATE_YAML")
            .setGroupId("me{}lucko{}configurate")
            .setArtifactId("configurate-yaml")
            .setVersion("3.5")
            .setChecksum("Dxr1o3EPbpOOmwraqu+cors8O/nKwJnhS5EiPkTb3fc=")
            .setAutoLoad(true)
            .build();

    public static final Dependency CONFIGURATE_GSON = Dependency
            .builder("CONFIGURATE_GSON")
            .setGroupId("me{}lucko{}configurate")
            .setArtifactId("configurate-gson")
            .setVersion("3.5")
            .setChecksum("Q3wp3xpqy41bJW3yUhbHOzm+NUkT4bUUBI2/AQLaa3c=")
            .setAutoLoad(true)
            .build();

    public static final Dependency HOCON_CONFIG = Dependency
            .builder("HOCON_CONFIG")
            .setGroupId("com{}typesafe")
            .setArtifactId("config")
            .setVersion("1.3.3")
            .setChecksum("tfHWBx8VSNBb6C9Z+QOcfTeheHvY48Z34x7ida9KRiE=")
            .setAutoLoad(true)
            .build();

    public static final Dependency CONFIGURATE_HOCON = Dependency
            .builder("CONFIGURATE_HOCON")
            .setGroupId("me{}lucko{}configurate")
            .setArtifactId("configurate-hocon")
            .setVersion("3.5")
            .setChecksum("sOym1KPmQylGSfk90ZFqobuvoZfEWb7XMmMBwbHuxFw=")
            .setAutoLoad(true)
            .build();

    public static final Dependency TOML4J = Dependency
            .builder("TOML4J")
            .setGroupId("com{}moandjiezana{}toml")
            .setArtifactId("toml4j")
            .setVersion("0.7.2")
            .setChecksum("9UdeY+fonl22IiNImux6Vr0wNUN3IHehfCy1TBnKOiA=")
            .setAutoLoad(true)
            .build();

    public static final Dependency CONFIGURATE_TOML = Dependency
            .builder("CONFIGURATE_TOML")
            .setGroupId("me{}lucko{}configurate")
            .setArtifactId("configurate-toml")
            .setVersion("3.5")
            .setChecksum("U8p0XSTaNT/uebvLpO/vb6AhVGQDYiZsauSGB9zolPU=")
            .setAutoLoad(true)
            .build();

    public static final Dependency MARIADB_DRIVER = Dependency
            .builder("MARIADB_DRIVER")
            .setGroupId("org{}mariadb{}jdbc")
            .setArtifactId("mariadb-java-client")
            .setVersion("2.2.5")
            .setChecksum("kFfgzoMFrFKirAFh/DgobV7vAu9NhdnhZLHD4/PCddI=")
            .setAutoLoad(true)
            .build();

    public static final Dependency MYSQL_DRIVER = Dependency
            .builder("MYSQL_DRIVER")
            .setGroupId("mysql")
            .setArtifactId("mysql-connector-java")
            .setVersion("5.1.46")
            .setChecksum("MSIIl2HmQD8C6Kge1KLWWi4QKXNGUboA8uqS2SD/ex4=")
            .setAutoLoad(true)
            .build();

    public static final Dependency POSTGRESQL_DRIVER = Dependency
            .builder("POSTGRESQL_DRIVER")
            .setGroupId("org{}postgresql")
            .setArtifactId("postgresql")
            .setVersion("9.4.1212")
            .setChecksum("DLKhWL4xrPIY4KThjI89usaKO8NIBkaHc/xECUsMNl0=")
            .setAutoLoad(true)
            .build();

    public static final Dependency MONGODB_DRIVER = Dependency
            .builder("MONGODB_DRIVER")
            .setGroupId("org.mongodb")
            .setArtifactId("mongo-java-driver")
            .setVersion("3.7.1")
            .setChecksum("yllBCqAZwWCNUoMPR0JWilqhVA46+9F47wIcnYOcoy4=")
            .setAutoLoad(true)
            .build();

    public static final Dependency H2_DRIVER = Dependency
            .builder("H2_DRIVER")
            .setGroupId("com.h2database")
            .setArtifactId("h2")
            .setVersion("1.4.197")
            .setChecksum("N/UhbhSvJ3KTDf+bhzQ1PwqA6Juj8z4GVEHeZTfF6EI=")
            .setAutoLoad(true)
            .build();

    public static final Dependency SQLITE_DRIVER = Dependency
            .builder("SQLITE_DRIVER")
            .setGroupId("org.xerial")
            .setArtifactId("sqlite-jdbc")
            .setVersion("3.27.2.1")
            .setChecksum("SxCoOLnNH9btS1lJZsfHsNFcLUxcmrqYzSjshgLraS0")
            .setAutoLoad(true)
            .build();

    public static final Dependency HIKARI = Dependency
            .builder("HIKARI")
            .setGroupId("com{}zaxxer")
            .setArtifactId("HikariCP")
            .setVersion("3.2.0")
            .setChecksum("sAjeaLvYWBH0tujwhg0JZsastPLnX6vUbsIJRWnL7+s=")
            .setAutoLoad(true)
            .build();

    /*public static final Dependency COMMONS_COLLECTIONS4 = Dependency
            .builder("COMMONS_COLLECTIONS4")
            .setGroupId("org{}apache{}commons")
            .setArtifactId("commons-collections4")
            .setVersion("4.0")
            .setChecksum("k/jfzSCDGijQkkJ3I/aWvOtwso5/uJ15FPFNXqSSzlo=")
            .setAutoLoad(true)
            .build();

    public static final Dependency COMMONS_IO = Dependency
            .builder("COMMONS_IO")
            .setGroupId("commons-io")
            .setArtifactId("commons-io")
            .setVersion("2.6")
            .setChecksum("+HfTBGYKwqFC84ZbrfyXHex+1zx0fH+NXS9ROcpzZRM=")
            .setAutoLoad(true)
            .build();

    public static final Dependency GNU_TROVE = Dependency
            .builder("GNU_TROVE")
            .setURL("http://zoidberg.ukp.informatik.tu-darmstadt.de/artifactory/public-releases/%s/%s/%s/%s-%s.jar")
            .setGroupId("gnu.trove")
            .setArtifactId("trove")
            .setVersion("3.0.3")
            .setChecksum("PIYWID1hoSp+NIfos088GYwrW6npDaDH6jLZnNSVgBI=")
            .setAutoLoad(true)
            .build();

    public static final Dependency APACHE_HTTP_CORE = Dependency
            .builder("APACHE_HTTP_CORE")
            .setGroupId("org{}apache{}httpcomponents")
            .setArtifactId("httpcore")
            .setVersion("4.4.12")
            .setChecksum("q3ZTNL6r8OoCRISl6Qp8QOgWCxRfItGZ4R4n9o1X2gg=")
            .setAutoLoad(true)
            .build();

    public static final Dependency APACHE_HTTP_CLIENT = Dependency
            .builder("APACHE_HTTP_CLIENT")
            .setGroupId("org{}apache{}httpcomponents")
            .setArtifactId("httpclient")
            .setVersion("4.5.10")
            .setChecksum("OLnxb1BJKOTbc2pDO5zRCWjZ7I1vXQ5hpkiJpokXITQ=")
            .setAutoLoad(true)
            .build();

    public static final Dependency WEBSOCKET_CLIENT = Dependency
            .builder("WEBSOCKET_CLIENT")
            .setGroupId("com{}neovisionaries")
            .setArtifactId("nv-websocket-client")
            .setVersion("1.6")
            .setChecksum("uOk+RVEtD3xH7+N8stl2B0CAFkm/dHW0yQIMDXrRlmQ=")
            .setAutoLoad(true)
            .build();

    public static final Dependency LOG4J_API = Dependency
            .builder("LOG4J_API")
            .setGroupId("org{}apache{}logging{}log4j")
            .setArtifactId("log4j-api")
            .setVersion("2.12.1")
            .setChecksum("QpU00Dvbcoh5q1UdRp4m9vf/TIqGJ/Waxoq27yYGNRU=")
            .setAutoLoad(true)
            .build();

    public static final Dependency LOG4J_CORE = Dependency
            .builder("LOG4J_CORE")
            .setGroupId("org{}apache{}logging{}log4j")
            .setArtifactId("log4j-core")
            .setVersion("2.12.1")
            .setChecksum("iF4xoU/HHLSEnpNWTSaiIcaFp4k3nvY8stCCzt88IjU=")
            .setAutoLoad(true)
            .build();

    public static final Dependency JDA = Dependency
            .builder("JDA")
            .setURL("https://jcenter.bintray.com/%s/%s/%s/%s-%s.jar")
            .setGroupId("net{}dv8tion")
            .setArtifactId("JDA")
            .setVersion("3.8.3_464")
            .setChecksum("YsYvZg14rVUCi8/0V+8v7l2oV36rWr6eu4InLmTrrqY=")
            .setAutoLoad(true)
            .build();

    public static final Dependency LAVA_COMMON = Dependency
            .builder("LAVA_COMMON")
            .setURL("https://jcenter.bintray.com/%s/%s/%s/%s-%s.jar")
            .setGroupId("com{}sedmelluq")
            .setArtifactId("lava-common")
            .setVersion("1.1.0")
            .setChecksum("VYwV5uX1jmk3D8CPHL+RdGc1K8Sd3qIWAyP1iuWy0pA=")
            .setAutoLoad(true)
            .build();

    public static final Dependency LAVA_PLAYER = Dependency
            .builder("LAVA_PLAYER")
            .setURL("https://jcenter.bintray.com/%s/%s/%s/%s-%s.jar")
            .setGroupId("com{}sedmelluq")
            .setArtifactId("lavaplayer")
            .setVersion("1.3.22")
            .setChecksum("JSizAxANMgastDqArEV+ssYeADYGb8KCczrauICzSds=")
            .setAutoLoad(true)
            .build();*/
}
