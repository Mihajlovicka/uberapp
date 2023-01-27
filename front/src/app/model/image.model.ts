interface ImageInterface {
  name: string;
  type: string;
  picByte: any;
}

export class Image implements ImageInterface {
  public name: string;
  public type: string;
  public picByte: any;

  constructor(imageInt: ImageInterface) {
    this.name = imageInt.name;
    this.type = imageInt.type;
    this.picByte = imageInt.picByte;
  }
}
