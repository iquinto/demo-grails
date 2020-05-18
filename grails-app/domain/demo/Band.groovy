package demo

class Band {
    Long id
    String name
    String yearFormed
    String yearDissolution
    String style
    String origin

    static mapping = {
        version false
    }

    static constraints = {
        id generator: 'increment'
        name nullable: false, blank: false
        yearFormed nullable: false, blank: false
        yearDissolution nullable: true
        style nullable: true
        origin nullable: false,  blank: false
    }
}
