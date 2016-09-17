package ourfood.domain.enums;

/**
 * Different crop produce grades (A/B/C)
 * <p>
 * IMPORTANT: The order of the enums should not be changed
 * </p>
 * 
 * @author raghu.mulukoju
 */
public enum ProduceGrade {
    NONE(0L), A(1L), B(2L), C(3L);

    Long produceGradeId;

    ProduceGrade(Long produceGradeId) {
        this.produceGradeId = produceGradeId;
    }

    public Long getProduceGrade() {
        return this.produceGradeId;
    }
}
