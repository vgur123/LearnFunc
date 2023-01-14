package lessons.lesson4.service;

import lessons.lesson4.CheckPerson;
import lessons.lesson4.model.Person;

public class CheckPersonEligibleForSelectiveService implements CheckPerson {
    @Override
    public boolean test(Person p) {
        return p.getGender() == Person.Sex.MALE &&
                p.getAge() >= 18 &&
                p.getAge() <= 25;
    }
}
