import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './event-inventory.reducer';
import { IEventInventory } from 'app/shared/model/event-inventory.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEventInventoryProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class EventInventory extends React.Component<IEventInventoryProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { eventInventoryList, match } = this.props;
    return (
      <div>
        <h2 id="event-inventory-heading">
          <Translate contentKey="misingpieceApp.eventInventory.home.title">Event Inventories</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="misingpieceApp.eventInventory.home.createLabel">Create new Event Inventory</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="misingpieceApp.eventInventory.itemCount">Item Count</Translate>
                </th>
                <th>
                  <Translate contentKey="misingpieceApp.eventInventory.creationDate">Creation Date</Translate>
                </th>
                <th>
                  <Translate contentKey="misingpieceApp.eventInventory.product">Product</Translate>
                </th>
                <th>
                  <Translate contentKey="misingpieceApp.eventInventory.event">Event</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {eventInventoryList.map((eventInventory, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${eventInventory.id}`} color="link" size="sm">
                      {eventInventory.id}
                    </Button>
                  </td>
                  <td>{eventInventory.itemCount}</td>
                  <td>
                    <TextFormat type="date" value={eventInventory.creationDate} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>
                    {eventInventory.product ? <Link to={`product/${eventInventory.product.id}`}>{eventInventory.product.id}</Link> : ''}
                  </td>
                  <td>{eventInventory.event ? <Link to={`event/${eventInventory.event.id}`}>{eventInventory.event.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${eventInventory.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${eventInventory.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${eventInventory.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ eventInventory }: IRootState) => ({
  eventInventoryList: eventInventory.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(EventInventory);
