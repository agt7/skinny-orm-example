# skinny-orm-example
Runnable sample code for Skinny-ORM.

実行可能なSkinny-ORMのサンプルです。

Person, Company, CompanyTypeという３つのテーブルで構成されています。これらのテーブルは親子孫という階層構造になっています。

通常は子のデータまでしか取れませんが、includesによって孫のデータまで取っています。
公式ドキュメントのEager Loadingと併せてご覧下さい。

# Result
```
22:24:11.079 [main] DEBUG scalikejdbc.ConnectionPool$ - Registered connection pool : ConnectionPool(url:jdbc:h2:file:./db/development;MODE=MySQL;AUTO_SERVER=TRUE, user:sa) using factory : commons-dbcp
22:24:11.951 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] drop table if exists person; (6 ms)
22:24:11.994 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] drop table if exists company; (10 ms)
22:24:12.001 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] drop table if exists company_type; (2 ms)
22:24:12.011 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] create table company_type (id bigint primary key, name varchar(64)); (5 ms)
22:24:12.041 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] create table company (id bigint primary key, name varchar(64), company_type_id bigint, foreign key (company_type_id) references company_type(id)); (27 ms)
22:24:12.069 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] create table person (id bigint primary key, name varchar(64), company_id bigint, foreign key (company_id) references company(id)); (5 ms)
22:24:12.073 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] insert into company_type(id, name) values (1, 'IT'); (0 ms)
22:24:12.077 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] insert into company(id, name, company_type_id) values (1, 'Company A', 1); (2 ms)
22:24:12.079 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] insert into person(id, name, company_id) values (1, 'seratch_ja', 1); (0 ms)
22:24:12.084 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] insert into person(id, name, company_id) values (2, 's_tsuka', NULL); (0 ms)
### Start print ###
22:24:12.620 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] select person.id as i_on_person, person.name as n_on_person, person.company_id as ci_on_person , company.id as i_on_company, company.name as n_on_company, company.company_type_id as cti_on_company from person left join company on person.company_id = company.id order by person.id; (0 ms)
22:24:12.684 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] select company.id as i_on_company, company.name as n_on_company, company.company_type_id as cti_on_company , company_type.id as i_on_company_type, company_type.name as n_on_company_type from company left join company_type on company.company_type_id = company_type.id where company.id in (1); (0 ms)
Person(1,seratch_ja,Some(1),Some(Company(1,Company A,Some(1),Some(CompanyType(1,IT)))))
Person(2,s_tsuka,None,None)
```
