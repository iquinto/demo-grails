package demo

import grails.gorm.services.Service

@Service(Band)
interface BandService {

    Band get(Serializable id)

    List<Band> list(Map args)

    Long count()

    void delete(Serializable id)

    Band save(Band band)

}