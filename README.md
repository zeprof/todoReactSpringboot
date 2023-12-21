# todoReactSpringboot

## Todo App with React and Spring Boot

# Pour connecter sur la base de données Azure, suivre les instructions suivantes:

https://learn.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-data-jpa-with-azure-postgresql?toc=%2Fazure%2Fpostgresql%2Ftoc.json&bc=%2Fazure%2Fbread%2Ftoc.json&tabs=passwordless%2Cservice-connector&pivots=postgresql-passwordless-flexible-server

# Dans les instructions qui créent le password via le fichier 'create_user.sql', il faut ajouter la ligne suivante
GRANT ALL PRIVILEGES ON SCHEMA public TO todouser;

# Pour intégrer le frontend React dans l'app Spring Boot, suivre les instructions suivantes:

https://medium.com/analytics-vidhya/how-to-package-your-react-app-with-spring-boot-41432be974bc
Dans le 'pom.xml'
```xml
    <dependencies>
        <dependency>
            <groupId>com.github.eirslett</groupId>
            <artifactId>frontend-maven-plugin</artifactId>
            <version>1.14.2</version>
        </dependency>
        <dependency>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-antrun-plugin</artifactId>
            <version>3.1.0</version>
        </dependency>
        <dependency>
            <groupId>com.azure.spring</groupId>
            <artifactId>spring-cloud-azure-starter-jdbc-postgresql</artifactId>
        </dependency>
    </dependencies>
```

Il faut également ajouter dans la section 'plugin' du pom

```xml
        <plugins>
            <plugin>
                <groupId>com.github.eirslett</groupId>
                <artifactId>frontend-maven-plugin</artifactId>
                <version>1.14.2</version>
                <configuration>
                    <workingDirectory>basic-frontend-app</workingDirectory>
                    <installDirectory>target</installDirectory>
                </configuration>
                <executions>
                    <execution>
                        <id>install node and npm</id>
                        <goals>
                            <goal>install-node-and-npm</goal>
                        </goals>
                        <configuration>
                            <nodeVersion>v21.1.0</nodeVersion>
                            <npmVersion>10.2.0</npmVersion>
                        </configuration>
                    </execution>
                    <execution>
                        <id>npm install</id>
                        <goals>
                            <goal>npm</goal>
                        </goals>
                        <configuration>
                            <arguments>install</arguments>
                        </configuration>
                    </execution>
                    <execution>
                        <id>npm run build</id>
                        <goals>
                            <goal>npm</goal>
                        </goals>
                        <configuration>
                            <arguments>run build</arguments>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <artifactId>maven-antrun-plugin</artifactId>
                <executions>
                    <execution>
                        <phase>generate-resources</phase>
                        <configuration>
                            <target>
                                <copy todir="${project.build.directory}/classes/static">
                                    <fileset dir="${project.basedir}/basic-frontend-app/build">
                                        <filename regex="^(?!index.html)" />
                                    </fileset>
                                </copy>
                                <copy file="${project.basedir}/basic-frontend-app/build/index.html"
                                      tofile="${project.build.directory}/classes/templates/index.html" />
                            </target>
                        </configuration>
                        <goals>
                            <goal>run</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
```
>Sur le déploiement Azure, il faut penser à mettre le profil Spring Boot '-Dspring.profiles.active=azure'
>Dans la configuration de l'app déployée.

>On peut maintenant utiliser des variables définies dans application.properties que l'on peut utilier dans React.
> Voir 'index.html' (De react) et IndexController de SpringBoot.  À noter que index.html doit être déployé dans /templates de SpringBoot.

>On peut rouler mvn package -DskipTests pour déployer l'appliation React à même SpringBoot. Tout devient donc accessible par la même addresse:port (ex: localohost:8080)

