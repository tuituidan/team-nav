export function saveStarCard(cardIds) {
  if (Array.isArray(cardIds) && cardIds.length > 0) {
    const sets = new Set(cardIds);
    if (localStorage.starCardIds) {
      localStorage.starCardIds.split(',').forEach(val => sets.add(val));
    }
    localStorage.starCardIds = Array.from(sets).join(',');
  }
}

export function buildStarCard(datas) {
  if (!localStorage.starCardIds) {
    return null;
  }
  const ids = localStorage.starCardIds.split(',');
  const stars = [];
  for (const data of datas) {
    if(data.id === '1'){
      continue;
    }
    if (data.cards) {
      cardStar(ids, data.cards, stars)
    } else if (data.children) {
      for (const child of data.children) {
        if (child.cards) {
          cardStar(ids, child.cards, stars)
        }
      }
    }
  }
  if (stars.length <= 0) {
    return null;
  }
  return {
    id: '1',
    cards: stars,
    cardCount: stars.length,
    flatSort: '0',
    sort: 0,
    name: '个人常用',
    icon: 'star',
    level: 1,
    valid: true,
    pid: '0'
  }
}

const cardStar = (ids, cards, stars) => {
  for (const card of cards) {
    if (ids.includes(card.id)) {
      card.star = true;
      stars.push({...card})
    }
  }
}
