import {DialogAction} from "./DialogAction";

export class DialogResult {
  action: DialogAction;
  obj: any;

  constructor(action: DialogAction, obj?: any) {
    this.action = action;
    this.obj = obj;
  }
}
