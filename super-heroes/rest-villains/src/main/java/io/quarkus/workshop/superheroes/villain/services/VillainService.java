package io.quarkus.workshop.superheroes.villain.services;

import io.quarkus.workshop.superheroes.villain.models.Villain;
import io.quarkus.workshop.superheroes.villain.util.RandomFunctions;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@ApplicationScoped
public class VillainService {

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Villain> findAll() {
        return Villain.listAll();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Villain findById(long id) {
        return Villain.findById(id);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Villain findRandom() {

        int length = (int) Villain.count();
        if (length == 0)
            return null;

        Villain randomVillain;
        do {
            Long id = (long) RandomFunctions.randint(0, length);
            randomVillain = Villain.findById(id);
        } while (randomVillain == null);

        return randomVillain;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Villain create(@Valid Villain villain) {
        villain.persistAndFlush();
        return villain;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Villain update(@Valid Villain oldVillain, @Valid Villain newVillain) {
        oldVillain.name      = newVillain.name;
        oldVillain.otherName = newVillain.otherName;
        oldVillain.picture   = newVillain.picture;
        oldVillain.powers    = newVillain.powers;
        oldVillain.level     = newVillain.level;
        return oldVillain;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Villain delete(@Valid Villain villain) {
        villain.delete();
        return villain;
    }
}
