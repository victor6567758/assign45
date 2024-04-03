state(bomb).
state(empty).
state(prize).


ocurrenceof([] , _,0).
ocurrenceof([H|T], H, NewCount):-
    ocurrenceof(T,H,OldCount),
    NewCount is OldCount +1.
%The first item in the list is different so keep old count
ocurrenceof([H|T], H2, Count):-
    dif(H, H2),
    ocurrenceof(T, H2, Count).

single_prize(Rooms) :-
    ocurrenceof(Rooms, prize, PrizeCount),
    PrizeCount =:= 1.

label_1(Rooms) :- nth1(1, Rooms, bomb), \+ label_3(Rooms).
label_2(Rooms) :- \+ nth1(2, Rooms, prize).
label_3(Rooms) :- label_2(Rooms), nth1(1, Rooms, prize).
label_4(Rooms) :- nth1(2, Rooms, empty),
    (nth1(1, Rooms, prize); nth1(3, Rooms, prize); nth1(5, Rooms, prize); nth1(7, Rooms, prize)).
label_5(Rooms) :- ocurrenceof(Rooms, empty, EmptyCount),
    EmptyCount =:= 3.
label_6(Rooms) :- ocurrenceof(Rooms, bomb,BombCount),
    BombCount =:= 5.
label_7(Rooms) :- nth1(6, Rooms, _), \+ nth1(6, Rooms, empty).
label_8(Rooms) :- nth1(7, Rooms, prize).

% tru if prize, fale if bomb, uncertain if empty
cond_1(Rooms) :- nth1(1, Rooms, prize), label_1(Rooms).
cond_1(Rooms) :- nth1(1, Rooms, bomb), \+ label_1(Rooms).
cond_1(Rooms) :- nth1(1, Rooms, empty).

cond_2(Rooms) :- nth1(2, Rooms, prize), label_2(Rooms).
cond_2(Rooms) :- nth1(2, Rooms, bomb), \+ label_2(Rooms).
cond_2(Rooms) :- nth1(2, Rooms, empty).

cond_3(Rooms) :- nth1(3, Rooms, prize), label_3(Rooms).
cond_3(Rooms) :- nth1(3, Rooms, bomb), \+ label_3(Rooms).
cond_3(Rooms) :- nth1(3, Rooms, empty).

cond_4(Rooms) :- nth1(4, Rooms, prize), label_4(Rooms).
cond_4(Rooms) :- nth1(4, Rooms, bomb), \+ label_4(Rooms).
cond_4(Rooms) :- nth1(4, Rooms, empty).

cond_5(Rooms) :- nth1(5, Rooms, prize), label_5(Rooms).
cond_5(Rooms) :- nth1(5, Rooms, bomb), \+ label_5(Rooms).
cond_5(Rooms) :- nth1(5, Rooms, empty).

cond_6(Rooms) :- nth1(6, Rooms, prize), label_6(Rooms).
cond_6(Rooms) :- nth1(6, Rooms, bomb), \+ label_6(Rooms).
cond_6(Rooms) :- nth1(6, Rooms, empty).

cond_7(Rooms) :- nth1(7, Rooms, prize), label_7(Rooms).
cond_7(Rooms) :- nth1(7, Rooms, bomb), \+ label_7(Rooms).
cond_7(Rooms) :- nth1(7, Rooms, empty).

cond_8(Rooms) :- nth1(8, Rooms, prize), label_8(Rooms).
cond_8(Rooms) :- nth1(8, Rooms, bomb), \+ label_8(Rooms).
cond_8(Rooms) :- nth1(8, Rooms, empty).

room_combinations(Rooms) :-
    length(Rooms, 8),
    maplist(state, Rooms),
    single_prize(Rooms),
    cond_1(Rooms),
    cond_2(Rooms),
    cond_3(Rooms),
    cond_4(Rooms),
    cond_5(Rooms),
    cond_6(Rooms),
    cond_7(Rooms),
    cond_8(Rooms).


