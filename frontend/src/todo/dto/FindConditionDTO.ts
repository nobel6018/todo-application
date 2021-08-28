import {FindConditionType} from "./FindConditionType";
import {TodoStatus} from "../domain/TodoStatus";

export interface FindConditionDTO {
    condition?: FindConditionType;
    by?: string | TodoStatus | Date;
}
