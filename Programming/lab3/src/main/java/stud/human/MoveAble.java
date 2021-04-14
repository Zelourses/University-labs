package stud.human;

import stud.place.Place;

public interface MoveAble {
    void turnUp(Place place,  VerbPart... verbParts);
    void turnUp(Place place);
    void roll(VerbPart... verbParts);
    void roll(Place place, VerbPart... verbParts);
    void go(Place place, VerbPart... verbParts);
    void go(Place place);
    void crawl(Place place);
    void crawl(Place place, VerbPart... verbParts);
    void toComeIn(Place place);
}
