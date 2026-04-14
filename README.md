# <align align="center"> 📈 SimulaInvest Desktop </align>

<p align="center">
  <img src="https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow?style=for-the-badge" alt="Status">
  <img src="https://img.shields.io/badge/Java-17%2B-blue?style=for-the-badge&logo=java" alt="Java">
  <img src="https://img.shields.io/badge/Framework-Spring%20Boot-brightgreen?style=for-the-badge&logo=springboot" alt="Spring Boot">
</p>

---

## 📝 Sobre o Projeto
O **SimulaInvest** é um sistema desenvolvido para auxiliar indivíduos que possuem dificuldades matemáticas e organizacionais no cálculo de juros compostos e na comparação de modalidades de investimento a médio e longo prazo. 

A aplicação foca em oferecer precisão nos cálculos e apoio à tomada de decisão financeira no quotidiano.

> [!IMPORTANT]
> Este projeto é desenvolvido no âmbito do **Projeto Temático I** da Universidade de Caxias do Sul (UCS).

---

## 🚀 Funcionalidades (MVP)
A versão inicial (Minimum Viable Product) compreende as seguintes funcionalidades essenciais:

* **Sistema de Login:** Acesso seguro para os utilizadores.
* **Registro de Perfis:** Gestão de modalidades de investimento (Renda Fixa, Tesouro Direto, etc).
* **Cálculo de Retorno:** Projeção de rendimentos com base em capital inicial, aportes regulares e taxas de juros.

---

## 🏗️ Arquitetura do Sistema
O software adota uma arquitetura robusta dividida em três camadas:

| Camada | Tecnologia | Descrição |
| :--- | :--- | :--- |
| **Apresentação** | JavaFX (FXML) | Interfaces gráficas e interação com o usuário. |
| **Lógica** | Spring Boot | Processamento matemático e regras de negócio. |
| **Dados** | Spring Data JPA | Persistência flexível (H2 e PostgreSQL). |

---

## 🔒 Modelo de Dados e Segurança
O sistema gerencia o registro de usuários e histórico através de **Mapeamento Objeto-Relacional (ORM)**.

* **Relacionamentos:** Utilização de chaves estrangeiras para associar usuários às suas simulações.
* **Segurança:** Implementação de criptografia para armazenamento de senhas e dados sensíveis.

---

<p align="center">
  Desenvolvido por <b>Anthony, Eduarda e Luiz</b> para a disciplina de Projeto Temático I - UCS
</p>
