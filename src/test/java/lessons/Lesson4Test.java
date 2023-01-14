package lessons;

import lessons.lesson4.CheckPerson;
import lessons.lesson4.model.Person;
import lessons.lesson4.service.CheckPersonEligibleForSelectiveService;
import lessons.lesson4.service.PersonService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Lesson4Test {
    List<Person> personList = new ArrayList<>();

    @Before
    public void initt() {
        personList.add(Person.builder().name("Vasia").age(19).gender(Person.Sex.MALE).build());
        personList.add(Person.builder().name("Petia").age(10).gender(Person.Sex.MALE).build());
        personList.add(Person.builder().name("Grisha").age(20).gender(Person.Sex.MALE).build());
    }

    @Test
    public void firstTest()  {
        PersonService.printPersonsWithPredicate(personList, new CheckPersonEligibleForSelectiveService());
    }

    @Test
    public void anonimousClassTest()  {
        PersonService.printPersonsWithPredicate(personList, new CheckPerson() {
            public boolean test(Person p) {
                return p.getGender() == Person.Sex.MALE
                        && p.getAge() >= 18
                        && p.getAge() <= 25;
            }
        });
    }
    @Test
    public void lambdaTest()  {
        PersonService.printPersonsWithPredicate(personList, p ->
                p.getGender() == Person.Sex.MALE
                && p.getAge() >= 18
                && p.getAge() <= 25);
    }

    @Test
    public void consumerTest()  {
        PersonService.processPersonsWithConsumer(personList,
                p -> p.getGender() == Person.Sex.MALE
                        && p.getAge() >= 18
                        && p.getAge() <= 25,
                Person::printPerson);
    }

    @Test
    public void functionTest()  {
        PersonService.processPersonsWithFunction(personList,
                p -> p.getGender() == Person.Sex.MALE
                        && p.getAge() >= 18
                        && p.getAge() <= 25,
                p -> p.getName(),
                email -> System.out.println(email)
        );
    }

    @Test
    public void genericTest()  {
        PersonService.processPersonsGeneric(
                personList,
                p -> p.getGender() == Person.Sex.MALE
                        && p.getAge() >= 18
                        && p.getAge() <= 25,
                p -> p.getName(),
                email -> System.out.println(email)
        );
    }
}
