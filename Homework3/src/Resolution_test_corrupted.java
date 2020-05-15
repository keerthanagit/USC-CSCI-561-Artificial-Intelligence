import java.util.*;

public class Resolution_test_corrupted {
    public static int counter = 0;

    public static Set<Sentence> notUnifiable = new HashSet<>();

    public String startResolution(KnowledgeBase kb, Sentence query) throws CloneNotSupportedException {
        try {
            for (Predicate term : query.getTerms()) {
                term.isNegated = !term.isNegated;
            }
            kb.addAtTop(query);
            System.out.println("KNOWLEDGE BASE :");
            kb.print();
            System.out.println("-----------------------------------------");
            Sentence result = unify(query, kb);
            if (result.getTerms().isEmpty())
                return Constants.SUCCESS;
            return Constants.FAILURE;
        } catch (StackOverflowError e) {
            //e.printStackTrace();
            return Constants.FAILURE;
        }

    }

    public Sentence resolve(Sentence p, Sentence q) {
        Sentence result = new Sentence();
        Predicate toRemove = new Predicate();
        //First add all the predicates of the first sentence in the new sentence
        result.addTerms(p.getTerms());
        for (Predicate predP : p.getTerms()) {
            boolean flag = false;
            for (Predicate predQ : q.getTerms()) {
                if (predP.name.equals(predQ.name) && predP.isNegated == !predQ.isNegated) {
                    int argumentMismatchCount = 0;
                    for (int i = 0; i < predP.arguments.size(); i++) {
                        if (!predP.arguments.get(i).getName().equals(predQ.arguments.get(i).getName())) {
                            argumentMismatchCount++;
                        }
                    }
                    //If there is any common predicate, save it
                    if (argumentMismatchCount == 0) {
                        flag = true;
                        toRemove = predQ;
                        break;
                    }
                }
            }
            //Remove the common predicate from the new sentence
            if (flag) {
                result.removeTerm(predP);
                break;
            }
        }
        //Add all the predicates of the second sentence except the common one in the new sentence
        for (Predicate predQ : q.getTerms()) {
            if (predQ != toRemove) {
                result.addTerm(predQ);
            }
        }
        //Return the new sentence
        return result;
    }

    public Sentence unify(Sentence query, KnowledgeBase kb) throws CloneNotSupportedException {
        counter++;
        System.out.println("Query is : " + query.print());
        //Check if empty sentence is returned
        if (query.getTerms().isEmpty())
            return query;
        //Check if the query is not unifiable
        if (notUnifiable.contains(query))
            return query;
        //Check for maximum number of resolutions
        if (counter == Constants.CUTOFF_DEPTH) {
            System.out.println("MAXIMUM NUMBER OF RESOLUTIONS REACHED!!!!!!!!!!!!!!!!!!");
            return query;
        }
        boolean foundSentence = false;
        //Check the query with each sentence in the KB. If there is any matching sentence, then unify with it
//        Collections.shuffle(query.getTerms());
        for (Predicate p : query.getTerms()) {
            for (Sentence temp : kb.getKnowledgeBase()) {
                HashMap<String, String> variableMapForP = new HashMap<>();
                HashMap<String, String> variableMapForQ = new HashMap<>();
                List<Set<String>> equalitySets = new ArrayList<>();
                Sentence fact = (Sentence) temp.clone();
                ////System.out.println("Sentence checking:\n\t" + query.print() + "\n\t" + fact.print());
                // Check if the sentence can be unified with query
                //for (Predicate p : query.getTerms()) {
                System.out.println("Current Query term considered: " + p.print());
                boolean flag = false;
                for (Predicate q : fact.getTerms()) {
                    //Check for only one matching predicate combination
                    if (isUnifiable(p, q, variableMapForP, variableMapForQ, equalitySets)) {
                        System.out.println("\tPredicate matched " + p.print() + " with " + q.print());
                        flag = true;
                        break;
                    }
                }
                if (flag)
                    break;
                // }
                // If yes, then substitute constants in place of variables
                if (!variableMapForP.isEmpty() || !variableMapForQ.isEmpty()) {
                    foundSentence = true;
                    System.out.println("Sentences matched:\n\t" + query.print() + "\n\t" + fact.print());
                    if (!variableMapForP.isEmpty()) {
                        Iterator itr = variableMapForP.entrySet().iterator();
                        while (itr.hasNext()) {
                            Map.Entry m = (Map.Entry) itr.next();
                            System.out.println("\tSubstitution:" + m.getKey() + "->" + m.getValue());
                            for (Predicate f : query.getTerms()) {
                                for (Argument a : f.arguments) {
                                    if (a.getType() == ArgumentType.Variable && a.getName().equals(m.getKey())) {
                                        a.setName((String) m.getValue());
                                        if (!Character.isUpperCase(((String) m.getValue()).charAt(0)))
                                            a.setType(ArgumentType.Variable);
                                        else
                                            a.setType(ArgumentType.Constant);
                                    }
                                }
                            }
                        }
                        System.out.println("Unified sentence is : " + query.print());
                    }
                    if (!variableMapForQ.isEmpty()) {
                        Iterator itr = variableMapForQ.entrySet().iterator();
                        while (itr.hasNext()) {
                            Map.Entry m = (Map.Entry) itr.next();
                            System.out.println("\tSubstitution:" + m.getKey() + "->" + m.getValue());
                            for (Predicate f : fact.getTerms()) {
                                for (Argument a : f.arguments) {
                                    if (a.getType() == ArgumentType.Variable && a.getName().equals(m.getKey())) {
                                        a.setName((String) m.getValue());
                                        if (!Character.isUpperCase(((String) m.getValue()).charAt(0)))
                                            a.setType(ArgumentType.Variable);
                                        else
                                            a.setType(ArgumentType.Constant);
                                    }
                                }
                            }
                        }
                        System.out.println("Unified sentence is : " + fact.print());
                    }
                    Sentence resolvedSentence = resolve(query, fact);
                    System.out.println("After resolution : " + resolvedSentence.print());
                    Sentence result = unify(resolvedSentence, kb);
                    if (result.getTerms().isEmpty()) {
                        query = result;
                        break;
                    }
                }
            }
        }
        if (!foundSentence)
            notUnifiable.add(query);
        return query;
    }

    public boolean isUnifiable(Predicate p, Predicate q, HashMap<String, String> variableMapForP, HashMap<String, String> variableMapForQ, List<Set<String>> equalitySets) {
        ////System.out.println("\tPredicate checking " + p.print() + " with " + q.print());
        //Check if the names of the predicates are equal and have opposite negation values
        if (q.name.equals(p.name) && q.isNegated == !p.isNegated) {
            int count = p.arguments.size();
            HashMap<String, String> varMapForP = new HashMap<>();
            HashMap<String, String> varMapForQ = new HashMap<>();
            //Iterate for all the arguments in both the predicates
            for (int i = 0; i < p.arguments.size(); i++) {
                Argument a1 = p.arguments.get(i);
                Argument a2 = q.arguments.get(i);
                // If argument 1 is a constant and argument 2 is a constant
                if (a1.getType() == ArgumentType.Constant && a2.getType() == ArgumentType.Constant) {
                    if (!a1.getName().equals(a2.getName())) {
                        //flag=true;
                        count--;
                        break;
                    } else {
                        varMapForP.put(a1.getName(), a2.getName());
                        varMapForQ.put(a2.getName(), a1.getName());
                    }

                }
                // If argument 1 is a variable and argument 2 is a constant
                else if (a1.getType() == ArgumentType.Variable && a2.getType() == ArgumentType.Constant) {
                    if (variableMapForP.containsKey(a1.getName())) {
                        if (!variableMapForP.get(a1.getName()).equals(a2.getName())) {
                            count--;
                            break;
                        }

                    } else {
                        if (varMapForP.containsKey(a1.getName()) && !varMapForP.get(a1.getName()).equals(a2.getName())) {
                            count--;
                            break;
                        } else if (!varMapForP.containsKey(a1.getName())) {
                            boolean flag = false;
                            for (Set<String> set : equalitySets) {
                                if (set.contains(a1.getName())) {
                                    replaceAllVariables(set, a2.getName(), varMapForP, varMapForQ, equalitySets);
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag)
                                varMapForP.put(a1.getName(), a2.getName());
                        }
                    }

                }
                // If argument 1 is a constant and argument 2 is a variable
                else if (a1.getType() == ArgumentType.Constant && a2.getType() == ArgumentType.Variable) {
                    if (variableMapForQ.containsKey(a2.getName())) {
                        if (!variableMapForQ.get(a2.getName()).equals(a1.getName())) {
                            count--;
                            break;
                        }
                    } else {
                        if (varMapForQ.containsKey(a2.getName()) && !varMapForQ.get(a2.getName()).equals(a1.getName())) {
                            count--;
                            break;
                        } else if (!varMapForQ.containsKey(a2.getName())) {
                            boolean flag = false;
                            for (Set<String> set : equalitySets) {
                                if (set.contains(a2.getName())) {
                                    replaceAllVariables(set, a1.getName(), varMapForP, varMapForQ, equalitySets);
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag)
                                varMapForQ.put(a2.getName(), a1.getName());
                        }
                    }
                }
                // If argument 1 is a variable and argument 2 is a variable
                else {
                    String vala1 = variableMapForP.containsKey(a1.getName()) ? variableMapForP.get(a1.getName()) : varMapForP.get(a1.getName());
                    String vala2 = variableMapForQ.containsKey(a2.getName()) ? variableMapForQ.get(a2.getName()) : varMapForQ.get(a2.getName());
                    addInEqualitySet(a1.getName(), a2.getName(), equalitySets);

                    if (vala1 == null && vala2 != null) {
                        replaceAllVariables(getEqualitySet(a1.getName(), equalitySets), vala2, varMapForP, varMapForQ, equalitySets);
                        //varMapForP.put(a1.getName(),vala2);
                    } else if (vala1 != null && vala2 == null) {
                        replaceAllVariables(getEqualitySet(a2.getName(), equalitySets), vala1, varMapForP, varMapForQ, equalitySets);
                        //varMapForQ.put(a2.getName(),vala1);
                    } else if (vala1 != null && vala2 != null) {
                        if (isConstant(vala1) && isConstant(vala2)) {
                            if (!vala1.equals(vala2)) {
                                count--;
                                break;
                            }

                        }

                    }

                    //Check for all variables condition

                    if (i == p.arguments.size() - 1 && !equalitySets.isEmpty()) {
                        //Come here
                        System.out.println("CAME HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        for (Set<String> set : equalitySets) {
                            replaceAllVariables(set, set.iterator().next(), varMapForP, varMapForQ, equalitySets);
                        }

                    }


                }
            }
            //If all the arguments of both the predicates match, then update the substitution set
            if (count == p.arguments.size()) {
                variableMapForP.putAll(varMapForP);
                variableMapForQ.putAll(varMapForQ);
                return true;
            }
        }
        // Return false if the predicates don't match
        return false;
    }

    private void replaceAllVariables(Set<String> set, String name, HashMap<String, String> varMapForP, HashMap<String, String> varMapForQ, List<Set<String>> equalitySets) {
        HashMap<String, String> varMap = new HashMap<>();
        for (String var : set) {
            varMap.put(var, name);
        }
        varMapForP.putAll(varMap);
        varMapForQ.putAll(varMap);
    }

    private void addInEqualitySet(String a1, String a2, List<Set<String>> equalitySets) {
        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();
        for (Set<String> set : equalitySets) {
            if (set.contains(a1))
                set1 = set;
            if (set.contains(a2))
                set2 = set;
        }
        if (set1.isEmpty() && set2.isEmpty()) {
            set1.add(a1);
            set1.add(a2);
            equalitySets.add(set1);
        } else if (set1.isEmpty() && !set2.isEmpty()) {
            set2.add(a1);
        } else if (!set1.isEmpty() && set2.isEmpty()) {
            set1.add(a2);
        } else {
            set1.addAll(set2);
            equalitySets.remove(set2);
        }

    }

    private Set<String> getEqualitySet(String a, List<Set<String>> equalitySets) {
        for (Set<String> set : equalitySets) {
            if (set.contains(a))
                return set;
        }
        return new HashSet<>();
    }

    private boolean isConstant(String name) {
        return Character.isUpperCase(name.charAt(0));
    }

}
