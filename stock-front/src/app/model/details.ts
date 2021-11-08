import {GainersDecliners} from "./gainersDecliners";
import {ShareValue} from "./shareValue";

export interface Details {
  gainersDecliners: GainersDecliners;
  mostActive: Array<ShareValue>;
  biggestTurnover: Array<ShareValue>;
}
