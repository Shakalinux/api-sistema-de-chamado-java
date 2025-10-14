package com.shakalinux.Web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

}

/**

 curl -X POST http://localhost:8080/api/chamados \
 -H "Content-Type: application/json" \
 -H "Authorization: Bearer " \
 -d '{
 "texto": "Minha impressora não conecta",
 "tipoChamado": "SUPORTE_TECNICO",
 "prioridadeChamado": "MEDIA"
 }'



 GET http://localhost:8080/api/chamados
 Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJocnByb2NoYTJAZ21haWwuY29tIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3NjAzMTE1MDAsImV4cCI6MTc2MDM5NzkwMH0.beU9hUCK-Xo3I48GxvPkNYWc4wM5rxqOR-pMTug5GXo


 curl -X POST http://localhost:8080/api/auth/login   -H "Content-Type: application/json"   -d '{
    "email": "shakalinux@gmail.com",
     "senha": "morena.2210"
    }'



 curl -X POST http://localhost:8080/api/users/register   -H "Content-Type: application/json"   -d '{
    "name": "shakalinux",
     "email": "shakalinux@gmail.com",
    "senha": "morena.2210",
    "role": "ADMIN"
}'

 eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaGFrYWxpbnV4QGdtYWlsLmNvbSIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc2MDMxNDc2NCwiZXhwIjoxNzYwNDAxMTY0fQ.SReJp6K1D-MmR7oYjyMSVBKMAFJe2aof3wQis0tpeBc







 curl -X PATCH "http://localhost:8080/api/chamados/252/status?status=EM_ATENDIMENTO" \
 -H "Authorization: Bearer  eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaGFrYWxpbnV4QGdtYWlsLmNvbSIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc2MDMxNDc2NCwiZXhwIjoxNzYwNDAxMTY0fQ.SReJp6K1D-MmR7oYjyMSVBKMAFJe2aof3wQis0tpeBc"





 curl http://localhost:8080/api/chamados/meus \
 -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJocnByb2NoYTJAZ21haWwuY29tIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3NjAzMTE1MDAsImV4cCI6MTc2MDM5NzkwMH0.beU9hUCK-Xo3I48GxvPkNYWc4wM5rxqOR-pMTug5GXo "


 curl -X POST http://localhost:8080/api/respostas \
 -H "Content-Type: application/json" \
 -H "Authorization: Bearer  eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaGFrYWxpbnV4QGdtYWlsLmNvbSIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc2MDMxNDc2NCwiZXhwIjoxNzYwNDAxMTY0fQ.SReJp6K1D-MmR7oYjyMSVBKMAFJe2aof3wQis0tpeBc" \
 -d '{
 "texto": "Verifiquei a impressora, ela precisa de atualização",
 "idChamado": 252
 }'

 curl http://localhost:8080/api/respostas/chamado/252 \
 -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJocnByb2NoYTJAZ21haWwuY29tIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3NjAzMTE1MDAsImV4cCI6MTc2MDM5NzkwMH0.beU9hUCK-Xo3I48GxvPkNYWc4wM5rxqOR-pMTug5GXo >"



 curl -X POST http://localhost:8080/api/users/register \
 -H "Content-Type: application/json" \
 -d '{
 "email": "hrprocha@gmail.com",
 "senha": "morena.2210",
 "nome": "Vinicius Amorim",
 "role": "USER"
 }'




 curl -X POST http://localhost:8080/api/users/register \
 -H "Content-Type: application/json" \
 -d '{
 "name": "Natanael Cordeiro",
 "email": "hrprocha@gmail.com",
 "senha": "morena.2210",
 "role": "USER"
 }'


 curl -X POST http://localhost:8080/api/auth/login \
 -H "Content-Type: application/json" \
 -d '{
 "email": "shakalinux@gmail.com",
 "senha": "morena.2210"
 }'

 eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJocnByb2NoYUBnbWFpbC5jb20iLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc2MDM4MDUxMiwiZXhwIjoxNzYwNDY2OTEyfQ.EWY_xfvFCAw2wBGjrFBndAFTW9dBh-cCnOdcvElToI0

 curl -X POST http://localhost:8080/api/chamados \
 -H "Content-Type: application/json" \
 -H "Authorization: Bearer  eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJocnByb2NoYUBnbWFpbC5jb20iLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc2MDM4MDUxMiwiZXhwIjoxNzYwNDY2OTEyfQ.EWY_xfvFCAw2wBGjrFBndAFTW9dBh-cCnOdcvElToI0" \
 -d '{
 "texto": "Meu computador atualizou para o sistema windows 11 sozinho",
 "tipoChamado": "SUPORTE_TECNICO",
 "prioridadeChamado": "MEDIA"
 }'

 eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaGFrYWxpbnV4QGdtYWlsLmNvbSIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc2MDM4MDY1MCwiZXhwIjoxNzYwNDY3MDUwfQ.CuX_VKGWmxTwbPr92V9L849W0wpuOSpqYaGA2uW8FTs
 curl -X GET http://localhost:8080/api/chamados/usuario?email=hrprocha@gmail.com \
 -H "Authorization: Bearer  eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaGFrYWxpbnV4QGdtYWlsLmNvbSIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc2MDM4MDY1MCwiZXhwIjoxNzYwNDY3MDUwfQ.CuX_VKGWmxTwbPr92V9L849W0wpuOSpqYaGA2uW8FTs"

 **/