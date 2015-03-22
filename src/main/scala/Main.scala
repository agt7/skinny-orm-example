import scalikejdbc._
import skinny.orm.SkinnyCRUDMapper

case class CompanyType(id: Long, name: String)

object CompanyType extends SkinnyCRUDMapper[CompanyType] {
  override def defaultAlias = createAlias("company_type")

  override def extract(rs: WrappedResultSet, n: ResultName[CompanyType]): CompanyType = new CompanyType(
    id = rs.get(n.id),
    name = rs.get(n.name)
  )
}

case class Company(id: Long, name: String, companyTypeId: Option[Long], companyType: Option[CompanyType] = None)

object Company extends SkinnyCRUDMapper[Company] {
  override def defaultAlias = createAlias("company")

  override def extract(rs: WrappedResultSet, n: ResultName[Company]): Company = new Company(
    id = rs.get(n.id),
    name = rs.get(n.name),
    companyTypeId = rs.get(n.companyTypeId)
  )


  belongsTo[CompanyType](CompanyType, (c, ct) => c.copy(companyType = ct))

  /*
  val companyTypeRef =
    belongsTo[CompanyType](CompanyType, (c, ct) => c.copy(companyType = ct))
      .includes[CompanyType]((companies, companyTypes) => companies.map { c =>
      c.copy(companyType = Some(CompanyType(1, "aaa")))
    })
    */
}

case class Person(id: Long, name: String, companyId: Option[Long], company: Option[Company] = None)

object Person extends SkinnyCRUDMapper[Person] {
  override def defaultAlias = createAlias("person")

  override def extract(rs: WrappedResultSet, n: ResultName[Person]): Person = new Person(
    id = rs.get(n.id),
    name = rs.get(n.name),
    companyId = rs.get(n.companyId)
  )

  val companyRef =
    belongsTo[Company](Company, (p, c) => p.copy(company = c))
      .includes[Company](
        merge = (persons, companies) => persons.map { p =>
          companies.find(c => p.company.exists(_.id == c.id))
            .map(c => p.copy(company = Some(c)))
            .getOrElse(p)
        }
      )
}

object Main {
  implicit lazy val s = AutoSession

  def main(args: Array[String]): Unit = {
    init()

    println("### Start print ###")
    Person.includes(Person.companyRef).findAll().foreach(println)
  }

  private def init(): Unit = {
    skinny.DBSettings.initialize()

    sql"drop table if exists person".execute.apply()
    sql"drop table if exists company".execute.apply()
    sql"drop table if exists company_type".execute.apply()

    sql"create table company_type (id bigint primary key, name varchar(64))".execute.apply()
    sql"create table company (id bigint primary key, name varchar(64), company_type_id bigint, foreign key (company_type_id) references company_type(id))".execute.apply()
    sql"create table person (id bigint primary key, name varchar(64), company_id bigint, foreign key (company_id) references company(id))".execute.apply()

    sql"insert into company_type(id, name) values (1, 'IT')".execute.apply()
    sql"insert into company(id, name, company_type_id) values (1, 'Company A', 1)".execute.apply()
    sql"insert into person(id, name, company_id) values (1, 'seratch_ja', 1)".execute.apply()
    sql"insert into person(id, name, company_id) values (2, 's_tsuka', NULL)".execute.apply()
  }
}
