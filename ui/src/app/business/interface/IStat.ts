import {Observable} from 'rxjs';
import {Stat} from "../model/Stat";

export interface IStat {
    getOverallStat(email: string): Observable<Stat>;
}
